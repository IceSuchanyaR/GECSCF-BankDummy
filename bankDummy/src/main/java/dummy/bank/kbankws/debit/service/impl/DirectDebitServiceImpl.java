package gec.scf.dummy.bank.kbankws.debit.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import gec.scf.dummy.bank.entity.LogTransaction;
import gec.scf.dummy.bank.entity.MasterBankAccount;
import gec.scf.dummy.bank.entity.MasterBankCase;
import gec.scf.dummy.bank.entity.MasterMappingCode;
import gec.scf.dummy.bank.entity.MasterMappingCodeDetail;
import gec.scf.dummy.bank.entity.MasterMappingResponseException;
import gec.scf.dummy.bank.kbankws.Constants;
import gec.scf.dummy.bank.kbankws.account.domain.AccountBalanceResponse;
import gec.scf.dummy.bank.kbankws.account.repository.AccountRepository;
import gec.scf.dummy.bank.kbankws.account.repository.MasterMappingCodeRepository;
import gec.scf.dummy.bank.kbankws.account.service.impl.AccountServiceImpl;
import gec.scf.dummy.bank.kbankws.core.config.DelayProcess;
import gec.scf.dummy.bank.kbankws.debit.domain.DirectDebitRequest;
import gec.scf.dummy.bank.kbankws.debit.domain.DirectDebitResponse;
import gec.scf.dummy.bank.kbankws.debit.domain.TransactionStatus;
import gec.scf.dummy.bank.kbankws.debit.service.DirectDebitService;
import gec.scf.dummy.bank.kbankws.enums.AccountStatus;
import gec.scf.dummy.bank.kbankws.enums.AccountType;
import gec.scf.dummy.bank.kbankws.enums.BankCaseEnums;
import gec.scf.dummy.bank.kbankws.enums.LogType;
import gec.scf.dummy.bank.kbankws.enums.MappingStatus;
import gec.scf.dummy.bank.kbankws.enums.ServiceType;
import gec.scf.dummy.bank.kbankws.generator.service.impl.GeneratorCoreBankServiceImpl;
import gec.scf.dummy.bank.repository.bankcase.BankCaseRepository;
import gec.scf.dummy.bank.repository.logtransaction.LogTransactionRepository;
import gec.scf.dummy.bank.repository.mappingexception.MappingExceptionRepository;
import io.seruco.encoding.base62.Base62;

@Profile(Constants.Profiles.PRODUCTION)
@Service
public class DirectDebitServiceImpl implements DirectDebitService {

	@Override
	public Optional<DirectDebitResponse> createDirectDebit(String bankCode, DirectDebitRequest debitRequest) {
		MasterBankAccount findSourceAccNo = new MasterBankAccount();
		MasterBankAccount findDestinationAccNo = new MasterBankAccount();
		DirectDebitResponse directDebitResponse = new DirectDebitResponse();
		MasterMappingCode masterMappingCode = new MasterMappingCode();
		MasterMappingResponseException mappingResponseException = new MasterMappingResponseException();
		Map<String, MasterMappingCodeDetail> mappingCodeDetailDefaults = new HashMap<>();
		Map<String, MasterMappingCodeDetail> mappingCodeDetails = new HashMap<>();
		boolean isWriteLogTransaction = true;
		try {
			// Step 1 : Check Request Parameter Not Null
			Map<String, Object> resultValidate = isInvalidInputParameter(debitRequest, bankCode);
			boolean isInValid = (Boolean) resultValidate.get(RESULT_KEY);
			if (isInValid) {
				isWriteLogTransaction = false;
				return Optional.of(invalidInputParameterResponse(directDebitResponse, resultValidate));
			}

			// Decode Parameter
			Base62 base62 = Base62.createInstance();
			final byte[] decoded = base62.decode(bankCode.getBytes());
			String bank_code_encode = new String(decoded);

			// Step 2: Check MasterMapping Response Exception
			final String serviceType = ServiceType.DIRECTDEBIT.getCode();
			mappingResponseException = mappingExceptionRepository.findMappingExceptionByBankCodeAccNoServiceType(
					bank_code_encode, debitRequest.getSourceAccountNo(), serviceType, EXCEPTION_STATUS);
			if (mappingResponseException != null) {
				isWriteLogTransaction = false;
				int delay = mappingResponseException.getDelay();
				DelayProcess.delay(delay);
				return Optional.of(prepareExceptionResponse(debitRequest, directDebitResponse, mappingResponseException,
						bank_code_encode));
			}

			// Step 3 : Check Mapping Code
			masterMappingCode = masterMappingCodeRepository.findMasterMappingCodeByBankCode(bank_code_encode,
					serviceType);
			mappingCodeDetailDefaults = prepareMappingCodeDetail(null);
			mappingCodeDetails = prepareMappingCodeDetail(masterMappingCode);
			if (masterMappingCode == null) {
				masterMappingCode = new MasterMappingCode();
				masterMappingCode.setDelay(0);
			}

			// Step 4 : Check TransactionNo is Duplicate
			if (isDuplicateTransactionNo(debitRequest)) {
				// MappingCode - Case Fail Duplicate TransactionNo
				isWriteLogTransaction = false;
				MasterMappingCodeDetail mappingCodeDetailCaseDuplicateTransactionNo = mappingCodeDetails
						.get(BankCaseEnums.DIRECTDEBIT_FAILED_DUPLICATE_TRANSACTION.getCode());
				if (mappingCodeDetailCaseDuplicateTransactionNo == null) {
					mappingCodeDetailCaseDuplicateTransactionNo = mappingCodeDetailDefaults
							.get(BankCaseEnums.DIRECTDEBIT_FAILED_DUPLICATE_TRANSACTION.getCode());
				}
				// Step 10 : Return Response
				return Optional.of(prepareDirectDebitResponse(debitRequest, directDebitResponse,
						mappingCodeDetailCaseDuplicateTransactionNo, bank_code_encode));
			}
			DelayProcess.delay(masterMappingCode.getDelay());
			findSourceAccNo = accountRepository
					.findMasterBankAccountByAccountNoAndBankCode(debitRequest.getSourceAccountNo(), bank_code_encode);

			// Step 5 : Check Bank Account of source account no
			if (isValidFindSourceAccNo(findSourceAccNo)) {
				// MappingCode - Case Fail Account Not Register
				MasterMappingCodeDetail mappingCodeDetailCaseAccountNotRegister = mappingCodeDetails
						.get(BankCaseEnums.DIRECTDEBIT_FAILED_SOURCEACC_NOT_REGISTER.getCode());
				if (mappingCodeDetailCaseAccountNotRegister == null) {
					mappingCodeDetailCaseAccountNotRegister = mappingCodeDetailDefaults
							.get(BankCaseEnums.DIRECTDEBIT_FAILED_SOURCEACC_NOT_REGISTER.getCode());
				}
				findSourceAccNo = new MasterBankAccount();
				// Step 10 : Return Response
				return Optional.of(prepareDirectDebitResponse(debitRequest, directDebitResponse,
						mappingCodeDetailCaseAccountNotRegister, bank_code_encode));
			}
			findDestinationAccNo = accountRepository.findMasterBankAccountByAccountNoAndBankCode(
					debitRequest.getDestinationAccountNo(), bank_code_encode);

			// Step 6 : Check Bank Account of destination account
			if (isValidFindDestinationAccNo(findDestinationAccNo)) {
				// MappingCode - Case Fail Account Not Register
				MasterMappingCodeDetail mappingCodeDetailCaseAccountNotRegister = mappingCodeDetails
						.get(BankCaseEnums.DIRECTDEBIT_FAILED_DESTACC_NOT_REGISTER.getCode());
				if (mappingCodeDetailCaseAccountNotRegister == null) {
					mappingCodeDetailCaseAccountNotRegister = mappingCodeDetailDefaults
							.get(BankCaseEnums.DIRECTDEBIT_FAILED_DESTACC_NOT_REGISTER.getCode());
				}
				findDestinationAccNo = new MasterBankAccount();
				// Step 10 : Return Response
				return Optional.of(prepareDirectDebitResponse(debitRequest, directDebitResponse,
						mappingCodeDetailCaseAccountNotRegister, bank_code_encode));
			}

			// Step 7 : Check Account Status is Suspend
			BigDecimal transactionAmount = new BigDecimal(debitRequest.getTransactionAmount());
			if (AccountStatus.SUSPEND.getCode().equals(findSourceAccNo.getAccountStatus())
					|| AccountStatus.SUSPEND.getCode().equals(findDestinationAccNo.getAccountStatus())) {
				MasterMappingCodeDetail mappingCodeDetailCaseAccountStatusNotActive = mappingCodeDetails
						.get(BankCaseEnums.DIRECTDEBIT_FAILED_ACCSTATUS_NOT_ACTIVE.getCode());
				if (mappingCodeDetailCaseAccountStatusNotActive == null) {
					mappingCodeDetailCaseAccountStatusNotActive = mappingCodeDetailDefaults
							.get(BankCaseEnums.DIRECTDEBIT_FAILED_ACCSTATUS_NOT_ACTIVE.getCode());
				}
				// Step 10 : Return Response
				return Optional.of(prepareDirectDebitResponse(debitRequest, directDebitResponse,
						mappingCodeDetailCaseAccountStatusNotActive, bank_code_encode));
			}

			// Step 8 : Check Available of source account no
			if (findSourceAccNo.getAvailable().compareTo(transactionAmount) < 0) {

				MasterMappingCodeDetail mappingCodeDetailCaseInsufficientFund = mappingCodeDetails
						.get(BankCaseEnums.DIRECTDEBIT_FAILED_INSUFFICIENT_FUND.getCode());
				if (mappingCodeDetailCaseInsufficientFund == null) {
					mappingCodeDetailCaseInsufficientFund = mappingCodeDetailDefaults
							.get(BankCaseEnums.DIRECTDEBIT_FAILED_INSUFFICIENT_FUND.getCode());
				}
				// Step 10 : Return Response
				return Optional.of(prepareDirectDebitResponse(debitRequest, directDebitResponse,
						mappingCodeDetailCaseInsufficientFund, bank_code_encode));
			}

			// Step 9 : Transfer
			processTransfer(findSourceAccNo, findDestinationAccNo, transactionAmount);

			MasterMappingCodeDetail mappingCodeDetailCaseTransferSuccess = mappingCodeDetails
					.get(BankCaseEnums.DIRECTDEBIT_SUCCESS.getCode());
			if (mappingCodeDetailCaseTransferSuccess == null) {
				mappingCodeDetailCaseTransferSuccess = mappingCodeDetailDefaults
						.get(BankCaseEnums.DIRECTDEBIT_SUCCESS.getCode());
			}
			// Step 10 : Return Response
			return Optional.of(prepareDirectDebitResponse(debitRequest, directDebitResponse,
					mappingCodeDetailCaseTransferSuccess, bank_code_encode));
		} catch (Exception e) {
			directDebitResponse.setTransactionNo(debitRequest.getTransactionNo());
			directDebitResponse.setDebitTime(simpleDateTimeFormat.format(new Date()));
			directDebitResponse.setTransactionStatus(TransactionStatus.ERROR);
			directDebitResponse.setFailureReasonCode(REASON_CODE_E99);
			directDebitResponse.setFailureReason(REASON_EXCEPTION);
			return Optional.of(directDebitResponse);
		} finally {
			// Step 11 : LogTransaction
			try {
				if (isWriteLogTransaction) {
					// Write Log Transaction
					saveLogTransaction(directDebitResponse, findSourceAccNo, findDestinationAccNo, debitRequest,
							LogType.DIRECTDEBIT.getCode());
				}
			} catch (Exception e) {
			}
		}
	}

	private DirectDebitResponse invalidInputParameterResponse(DirectDebitResponse directDebitResponse,
			Map<String, Object> resultValidate) {
		String resultParams = (String) resultValidate.get(RESULT_PARAMS);
		String resultTransactionNo = (String) resultValidate.get(TRANSACTION_NO);
		directDebitResponse.setTransactionNo(resultTransactionNo);
		directDebitResponse.setDebitTime(simpleDateTimeFormat.format(new Date()));
		directDebitResponse.setTransactionStatus(TransactionStatus.FAILED);
		directDebitResponse.setFailureReasonCode(REASON_CODE_E01);
		directDebitResponse.setFailureReason(String.format(REASON_ACC_INVALID, resultParams));
		return directDebitResponse;
	}

	@Override
	public Optional<DirectDebitResponse> getDirectDebit(String bankCode, DirectDebitRequest debit) {
		DirectDebitResponse directDebitResponse = new DirectDebitResponse();
		boolean isWriteLogTransaction = true;
		MasterMappingResponseException mappingResponseException = new MasterMappingResponseException();
		LogTransaction findLogtransaction = null;
		String bank_code_encode = "";
		try {
			// Step 1 : Check Invalid TransactionNo
			Map<String, Object> resultValidate = isInvalidDebitRequest(bankCode, debit);
			boolean isInValid = (Boolean) resultValidate.get(RESULT_KEY);
			if (isInValid) {
				isWriteLogTransaction = false;
				String resultParams = (String) resultValidate.get(RESULT_PARAMS);
				directDebitResponse.setTransactionNo(debit.getTransactionNo());
				directDebitResponse.setDebitTime(simpleDateTimeFormat.format(new Date()));
				directDebitResponse.setTransactionStatus(TransactionStatus.FAILED);
				directDebitResponse.setFailureReasonCode(REASON_CODE_E01);
				directDebitResponse.setFailureReason(String.format(REASON_ACC_INVALID, resultParams));
				// Step 3 : Return Response
				return Optional.of(directDebitResponse);
			}
			// Decode Parameter
			Base62 base62 = Base62.createInstance();
			final byte[] bank_code_decoded = base62.decode(bankCode.getBytes());
			bank_code_encode = new String(bank_code_decoded);
			final byte[] transaction_no_decoded = base62.decode(debit.getTransactionNo().getBytes());
			String transaction_no_encode = new String(transaction_no_decoded);
			// Step 2 : Transaction No is not found
			findLogtransaction = logTransactionRepository.findLogTransactionByTransactionNoAndBankCode(
					transaction_no_encode, LogType.DIRECTDEBIT.getCode());
			if (findLogtransaction == null) {
				isWriteLogTransaction = false;
				directDebitResponse.setTransactionNo(transaction_no_encode);
				directDebitResponse.setDebitTime(simpleDateTimeFormat.format(new Date()));
				directDebitResponse.setTransactionStatus(TransactionStatus.FAILED);
				directDebitResponse.setFailureReasonCode(REASON_CODE_104);
				directDebitResponse.setFailureReason(String.format(REASON_ACC_INVALID, "Transaction No is not found"));
				// Step 3 : Return Response
				return Optional.of(directDebitResponse);
			}
			final String serviceType = ServiceType.ENQUIRY_DIRECT_DEBIT.getCode();
			mappingResponseException = mappingExceptionRepository.findMappingExceptionByBankCodeAccNoServiceType(
					bank_code_encode, findLogtransaction.getSourceAccountNo(), serviceType, EXCEPTION_STATUS);
			if (mappingResponseException != null) {
				isWriteLogTransaction = false;
				int delay = mappingResponseException.getDelay();
				DelayProcess.delay(delay);
				directDebitResponse.setTransactionNo(transaction_no_encode);
				directDebitResponse.setDebitTime(simpleDateTimeFormat.format(new Date()));
				directDebitResponse.setTransactionStatus(
						prepareTransactionStatus(mappingResponseException.getTransactionStatus()));
				directDebitResponse.setFailureReason(mappingResponseException.getFailureReason());
				directDebitResponse.setFailureReasonCode(mappingResponseException.getFailureReasonCode());
				return Optional.of(directDebitResponse);
			} else {
				// Step 3 : Return Response
				return Optional.of(prepareEnquiryDirectDebitResponse(directDebitResponse, findLogtransaction,
						transaction_no_encode));
			}
		} catch (Exception e) {
			// Step 3 : Return Response
			directDebitResponse.setTransactionNo(debit.getTransactionNo());
			directDebitResponse.setDebitTime(simpleDateTimeFormat.format(new Date()));
			directDebitResponse.setTransactionStatus(TransactionStatus.ERROR);
			directDebitResponse.setFailureReasonCode(REASON_CODE_E99);
			directDebitResponse.setFailureReason(String.format(REASON_EXCEPTION, "Enquiry Direct Debit."));
			return Optional.of(directDebitResponse);
		} finally {
			// Step 4 : LogTransaction
			try {
				if (isWriteLogTransaction) {
					// Write Log Transaction
					prepareSaveLogTransactionEnquiryDirectDebit(directDebitResponse, findLogtransaction, debit);
				}
			} catch (Exception e) {
			}
		}
	}

	private Map<String, Object> isInvalidDebitRequest(String bankCode, DirectDebitRequest debit) {
		Map<String, Object> result = new HashMap<>();
		result.put(RESULT_KEY, false);
		result.put(RESULT_PARAMS, "");
		if (debit == null) {
			result.put(RESULT_KEY, true);
			result.put(RESULT_PARAMS, "DebitRequest is NULL");
			return result;
		} else {
			if (debit.getTransactionNo() == null || "".equals(debit.getTransactionNo())) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "TransactionNo is NULL");
				return result;
			}
			if (debit.getTransactionNo().length() > 50) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "TransactionNo must no more than 20 digits.");
				return result;
			}
			if (bankCode == null || "".equals(bankCode)) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "BankCode is NULL");
				return result;
			}
			if (bankCode.length() > 50) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "BankCode must no more than 50 digits.");
				return result;
			}
		}
		return result;
	}

	private void prepareSaveLogTransactionEnquiryDirectDebit(DirectDebitResponse directDebitResponse,
			LogTransaction findLogtransaction, DirectDebitRequest debit) throws Exception {
		try {
			MasterBankAccount findSourceAccNo = new MasterBankAccount();
			findSourceAccNo.setBankCode(findLogtransaction.getBankCode());
			findSourceAccNo.setCustomerID(findLogtransaction.getSourceCustomerID());
			findSourceAccNo.setAccountType(findLogtransaction.getSourceAccountType());

			MasterBankAccount findDestinationAccNo = new MasterBankAccount();
			findDestinationAccNo.setCustomerID(findLogtransaction.getDestinationCustomerID());
			findDestinationAccNo.setAccountType(findLogtransaction.getDestinationAccountType());

			debit.setSourceAccountNo(findLogtransaction.getSourceAccountNo());
			debit.setDestinationAccountNo(findLogtransaction.getDestinationAccountNo());
			debit.setTransactionDate(findLogtransaction.getTransactionDate().toString());
			debit.setTransactionAmount(findLogtransaction.getTransactionAmount());
			debit.setCurrencyCode(findLogtransaction.getCurrencyCode());

			saveLogTransaction(directDebitResponse, findSourceAccNo, findDestinationAccNo, debit,
					LogType.ENQUIRY_DIRECT_DEBIT.getCode());
		} catch (Exception e) {
			throw e;
		}
	}

	private void saveLogTransaction(DirectDebitResponse directDebitResponse, MasterBankAccount findSourceAccNo,
			MasterBankAccount findDestinationAccNo, DirectDebitRequest debitRequest, String logType) throws Exception {
		try {
			LogTransaction log = new LogTransaction();
			log.setLogType(logType);
			log.setTransactionNo(directDebitResponse.getTransactionNo());
			log.setBankCode(findSourceAccNo.getBankCode());
			log.setSourceCustomerID(findSourceAccNo.getCustomerID());
			log.setSourceAccountType(findSourceAccNo.getAccountType());
			log.setSourceAccountNo(debitRequest.getSourceAccountNo());
			log.setDestinationCustomerID(findDestinationAccNo.getCustomerID());
			log.setDestinationAccountNo(debitRequest.getDestinationAccountNo());
			log.setDestinationAccountType(findDestinationAccNo.getAccountType());
			log.setTransactionDate(simpleDateFormat.parse(debitRequest.getTransactionDate()));
			log.setTransactionStatus(directDebitResponse.getTransactionStatus().getCode());
			log.setTransactionAmount(debitRequest.getTransactionAmount());
			log.setFailureReason(directDebitResponse.getFailureReason());
			log.setFailureReasonCode(directDebitResponse.getFailureReasonCode());
			log.setCurrencyCode(debitRequest.getCurrencyCode());
			log.setBankTransactionNo(directDebitResponse.getBankTransactionNo());
			Date currentTime = new Date();
			log.setUpdateTime(currentTime);
			logTransactionRepository.save(log);
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	
	

	private Map<String, MasterMappingCodeDetail> prepareMappingCodeDetail(MasterMappingCode masterMappingCode) {
		Map<String, MasterMappingCodeDetail> results = new HashMap<>();
		try {
			if (masterMappingCode == null || masterMappingCode.getMasterMappingCodeDetail().isEmpty()) {
				List<MasterMappingCodeDetail> mappingCodesDetail = initialBankCase();
				for (MasterMappingCodeDetail detail : mappingCodesDetail) {
					results.put(detail.getBankCaseCode(), detail);
				}
			} else {
				for (MasterMappingCodeDetail detail : masterMappingCode.getMasterMappingCodeDetail()) {
					results.put(detail.getBankCaseCode(), detail);
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return results;
	}

	private List<MasterMappingCodeDetail> initialBankCase() {
		List<MasterMappingCodeDetail> masterMappingCodeDetail = new ArrayList<>();
		try {
			List<MasterBankCase> bankCase = bankCaseRepository
					.findBankCaseByBankCaseGroup(ServiceType.DIRECTDEBIT.getCode());
			if (!bankCase.isEmpty()) {
				for (MasterBankCase each : bankCase) {
					masterMappingCodeDetail.add(new MasterMappingCodeDetail(1L, each.getBankCaseCode(),
							each.getBankCaseStatus(), each.getDefaultReasonCode(), each.getBankCaseName()));
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return masterMappingCodeDetail;
	}

	private void processTransfer(MasterBankAccount findSourceAccNo, MasterBankAccount findDestinationAccNo,
			BigDecimal transactionAmount) {
		try {
			MasterBankAccount sourceAcc = updateSourceAccBalance(findSourceAccNo, transactionAmount);
			MasterBankAccount destinationAcc = updateDestinationAccBalance(findDestinationAccNo, transactionAmount);

			accountRepository.save(sourceAcc);
			accountRepository.save(destinationAcc);
		} catch (Exception e) {
			throw e;
		}
	}

	private MasterBankAccount updateSourceAccBalance(MasterBankAccount findSourceAccNo, BigDecimal transactionAmount) {
		try {
			Date updateTime = new Date();
			BigDecimal sourceLedgerBalanceAmt = findSourceAccNo.getLedgerBalance().subtract(transactionAmount);
			BigDecimal sourceOutstandingAmt = sourceLedgerBalanceAmt.multiply(new BigDecimal("-1.00"));
			BigDecimal sourceAvailableAmt = findSourceAccNo.getCreditLimit().add(sourceLedgerBalanceAmt);
			if (sourceLedgerBalanceAmt.compareTo(new BigDecimal("0.00")) > 0) {
				sourceOutstandingAmt = new BigDecimal("0.00");
			}
			findSourceAccNo.setOutstanding(sourceOutstandingAmt);
			findSourceAccNo.setLedgerBalance(sourceLedgerBalanceAmt);
			findSourceAccNo.setAvailable(sourceAvailableAmt);
			findSourceAccNo.setUpdateBy(0L);
			findSourceAccNo.setUpdateTime(updateTime);
		} catch (Exception e) {
			throw e;
		}
		return findSourceAccNo;
	}

	private MasterBankAccount updateDestinationAccBalance(MasterBankAccount findDestinationAccNo,
			BigDecimal transactionAmount) {
		try {
			Date updateTime = new Date();
			BigDecimal destinationLedgerBalanceAmt = findDestinationAccNo.getLedgerBalance().add(transactionAmount);
			BigDecimal destinationOutstandingAmt = findDestinationAccNo.getLedgerBalance()
					.multiply(new BigDecimal("-1.00"));
			BigDecimal destinationAvailableAmt = findDestinationAccNo.getCreditLimit().add(destinationLedgerBalanceAmt);
			if (destinationLedgerBalanceAmt.compareTo(new BigDecimal("0.00")) > 0) {
				destinationOutstandingAmt = new BigDecimal("0.00");
			}
			findDestinationAccNo.setOutstanding(destinationOutstandingAmt);
			findDestinationAccNo.setLedgerBalance(destinationLedgerBalanceAmt);
			findDestinationAccNo.setAvailable(destinationAvailableAmt);
			findDestinationAccNo.setUpdateBy(0L);
			findDestinationAccNo.setUpdateTime(updateTime);
		} catch (Exception e) {
			throw e;
		}
		return findDestinationAccNo;
	}

	private boolean isDuplicateTransactionNo(DirectDebitRequest debitRequest) {
		boolean result = false;
		try {
			LogTransaction findTransactionNo = logTransactionRepository
					.findLogTransactionByTransactionNo(debitRequest.getTransactionNo(), LogType.DIRECTDEBIT.getCode());
			if (findTransactionNo != null) {
				result = true;
			}
		} catch (Exception e) {
			return result;
		}
		return result;
	}

	private Map<String, Object> isInvalidInputParameter(DirectDebitRequest debitRequest, String bankCode) {
		Map<String, Object> result = new HashMap<>();
		result.put(RESULT_KEY, false);
		result.put(RESULT_PARAMS, "");
		result.put(TRANSACTION_NO, null);
		if (debitRequest == null) {
			result.put(RESULT_KEY, true);
			result.put(RESULT_PARAMS, "DebitRequest is NULL");
			return result;
		} else {
			if (debitRequest.getTransactionNo() == null || "".equals(debitRequest.getTransactionNo())) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "TransactionNo is NULL");
				return result;
			}
			result.put(TRANSACTION_NO, debitRequest.getTransactionNo());
			if (debitRequest.getTransactionNo().length() > 20) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "TransactionNo must no more than 20 digits.");
				return result;
			}
			if (bankCode == null || "".equals(bankCode)) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "BankCode is NULL");
				return result;
			}
			if (bankCode.length() > 50) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "BankCode must no more than 50 digits.");
				return result;
			}
			if (debitRequest.getSourceAccountNo() == null || "".equals(debitRequest.getSourceAccountNo())) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "SourceAccountNo is NULL");
				return result;
			}
			if (debitRequest.getSourceAccountNo().length() > 20) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "SourceAccountNo must no more than 20 digits.");
				return result;
			}
			if (debitRequest.getDestinationAccountNo() == null || "".equals(debitRequest.getDestinationAccountNo())) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "DestinationAccountNo is NULL");
				return result;
			}
			if (debitRequest.getDestinationAccountNo().length() > 20) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "DestinationAccountNo must no more than 20 digits.");
				return result;
			}
			if (debitRequest.getTransactionDate() == null || "".equals(debitRequest.getTransactionDate())) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "TransactionDate is NULL");
				return result;
			}
			if (debitRequest.getTransactionDate().length() > 10) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "TransactionDate must no more than 10 digits.");
				return result;
			}
			if (isvalidateFormatDate(debitRequest.getTransactionDate())) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "TransactionDate is Invalid.");
				return result;
			}
			if (debitRequest.getTransactionAmount() == null || "".equals(debitRequest.getTransactionAmount())) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "TransactionAmount is NULL");
				return result;
			}
			if (debitRequest.getTransactionAmount().length() > 20) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "TransactionAmount must no more than 20 digits.");
				return result;
			}
			try {
				new BigDecimal(debitRequest.getTransactionAmount());

			} catch (Exception e) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "Invalid TransactionAmount Format. ");
				return result;
			}
			if (new BigDecimal(debitRequest.getTransactionAmount()).compareTo(BigDecimal.ZERO) <= 0) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "Invalid TransactionAmount Format must be greater than 0. ");
				return result;

			}
			if (debitRequest.getCurrencyCode() == null || "".equals(debitRequest.getCurrencyCode())) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "CurrencyCode is NULL.");
				return result;
			}
			if (debitRequest.getCurrencyCode().length() > 3) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "CurrencyCode must no more than 3 digits.");
				return result;
			}
		}
		return result;
	}

	private TransactionStatus prepareTransactionStatus(String transactionStatus) {
		for (TransactionStatus status : TransactionStatus.values()) {
			if (transactionStatus.equals(status.getCode())) {
				return status;
			}
		}
		return null;
	}

	private boolean isValidFindSourceAccNo(MasterBankAccount findSourceAccNo) {
		boolean result = false;
		try {
			if (findSourceAccNo == null || AccountType.TERM_LOAN.getCode().equals(findSourceAccNo.getAccountType())) {
				result = true;
			}
		} catch (Exception e) {
		}
		return result;
	}

	private boolean isValidFindDestinationAccNo(MasterBankAccount findDestinationAccNo) {
		boolean result = false;
		try {
			if (findDestinationAccNo == null
					|| AccountType.TERM_LOAN.getCode().equals(findDestinationAccNo.getAccountType())) {
				result = true;
			}
		} catch (Exception e) {
		}
		return result;
	}

	private AccountBalanceResponse getEnquiryAccountBalanceResponse(DirectDebitRequest directDebitRequest,
			String bankCode) {
		AccountBalanceResponse accountBalanceResponse = null;
		Base62 base62 = Base62.createInstance();
		final byte[] en_account = base62.encode(directDebitRequest.getSourceAccountNo().getBytes());
		String account_no_decode = new String(en_account);
		final byte[] de_bank_code = base62.encode(bankCode.getBytes());
		String bank_code_encode = new String(de_bank_code);
		try {
			Optional<AccountBalanceResponse> enquiryAccountBalanceResponse = accountServiceImpl
					.getAccountBalanceByAccountNo(bank_code_encode, account_no_decode);
			if (enquiryAccountBalanceResponse.isPresent()) {

				accountBalanceResponse = enquiryAccountBalanceResponse.get();

			}
		} catch (Exception e) {
			throw e;
		}
		return accountBalanceResponse;
	}

	private boolean isvalidateFormatDate(String transactionDate) {
		try {
			simpleDateFormat.parse(transactionDate);
			return false;
		} catch (Exception e) {
			return true;
		}
	}

	private DirectDebitResponse prepareExceptionResponse(DirectDebitRequest debitRequest,
			DirectDebitResponse directDebitResponse, MasterMappingResponseException mappingResponseException,
			String bankCode) {
		directDebitResponse.setTransactionNo(debitRequest.getTransactionNo());
		directDebitResponse.setDebitTime(simpleDateTimeFormat.format(new Date()));
		directDebitResponse.setSourceAccount(
				responseSourceAccNo(debitRequest, bankCode, mappingResponseException.getTransactionStatus()));
		directDebitResponse
				.setTransactionStatus(prepareTransactionStatus(mappingResponseException.getTransactionStatus()));
		directDebitResponse.setFailureReason(mappingResponseException.getFailureReason());
		directDebitResponse.setFailureReasonCode(mappingResponseException.getFailureReasonCode());
		return directDebitResponse;
	}

	private String generatorCode(ServiceType serviceType) {
		String configCodeGenerator = null;
		try {
			configCodeGenerator = generatorCoreBankServiceImpl.generateBankTransactionNo(serviceType);
			return configCodeGenerator;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return configCodeGenerator;
	}

	private DirectDebitResponse prepareDirectDebitResponse(DirectDebitRequest directDebitRequest,
			DirectDebitResponse directDebitResponse, MasterMappingCodeDetail mappingCodeDetail, String bankCode) {
		directDebitResponse.setTransactionNo(directDebitRequest.getTransactionNo());
		directDebitResponse.setDebitTime(simpleDateTimeFormat.format(new Date()));
		if (TransactionStatus.SUCCESS.getCode().equals(mappingCodeDetail.getTransactionStatus())) {
			mappingCodeDetail.setFailureReasonCode(null);
			mappingCodeDetail.setFailureReason(null);
			directDebitResponse.setBankTransactionNo(generatorCode(ServiceType.DIRECTDEBIT));
			directDebitResponse.setSourceAccount(getEnquiryAccountBalanceResponse(directDebitRequest, bankCode));
			directDebitResponse
					.setTransactionStatus(prepareTransactionStatus(mappingCodeDetail.getTransactionStatus()));
			directDebitResponse.setFailureReason(mappingCodeDetail.getFailureReason());
			directDebitResponse.setFailureReasonCode(mappingCodeDetail.getFailureReasonCode());
		} else {
			directDebitResponse.setSourceAccount(
					responseSourceAccNo(directDebitRequest, bankCode, mappingCodeDetail.getTransactionStatus()));
			directDebitResponse
					.setTransactionStatus(prepareTransactionStatus(mappingCodeDetail.getTransactionStatus()));
			directDebitResponse.setFailureReason(mappingCodeDetail.getFailureReason());
			directDebitResponse.setFailureReasonCode(mappingCodeDetail.getFailureReasonCode());
		}
		return directDebitResponse;
	}

	private DirectDebitResponse prepareEnquiryDirectDebitResponse(DirectDebitResponse directDebitResponse,
			LogTransaction findLogtransaction, String transactionNo) {
		if (TransactionStatus.SUCCESS.getCode().equals(findLogtransaction.getTransactionStatus())) {
			directDebitResponse.setTransactionNo(transactionNo);
			directDebitResponse.setDebitTime(simpleDateTimeFormat.format(new Date()));
			directDebitResponse.setBankTransactionNo(findLogtransaction.getBankTransactionNo());
			directDebitResponse
					.setTransactionStatus(prepareTransactionStatus(findLogtransaction.getTransactionStatus()));
			directDebitResponse.setFailureReasonCode(findLogtransaction.getFailureReasonCode());
			directDebitResponse.setFailureReason(findLogtransaction.getFailureReason());
		} else {
			directDebitResponse.setTransactionNo(transactionNo);
			directDebitResponse.setDebitTime(simpleDateTimeFormat.format(new Date()));
			directDebitResponse
					.setTransactionStatus(prepareTransactionStatus(findLogtransaction.getTransactionStatus()));
			directDebitResponse.setFailureReasonCode(findLogtransaction.getFailureReasonCode());
			directDebitResponse.setFailureReason(findLogtransaction.getFailureReason());
		}
		return directDebitResponse;
	}

	private AccountBalanceResponse responseSourceAccNo(DirectDebitRequest directDebitRequest, String bankCode,
			String exceptionStatus) {
		AccountBalanceResponse responseSourceAccNo = null;
		Base62 base62 = Base62.createInstance();
		final byte[] en_account = base62.encode(directDebitRequest.getSourceAccountNo().getBytes());
		String account_no_decode = new String(en_account);
		final byte[] de_bank_code = base62.encode(bankCode.getBytes());
		String bank_code_encode = new String(de_bank_code);
		try {
			Optional<AccountBalanceResponse> enquiryAccountBalanceResponse = accountServiceImpl
					.getAccountBalanceByAccountNo(bank_code_encode, account_no_decode);
			if (enquiryAccountBalanceResponse.isPresent()) {
				if (MappingStatus.SUCCESS.getCode().equals(exceptionStatus)) {
					responseSourceAccNo = enquiryAccountBalanceResponse.get();
				} else {
					responseSourceAccNo = new AccountBalanceResponse();
					responseSourceAccNo.setAccountNo(enquiryAccountBalanceResponse.get().getAccountNo());
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return responseSourceAccNo;
	}

	private static final String REASON_CODE_E01 = "E01";
	private static final String REASON_CODE_E99 = "E99";
	private static final String REASON_ACC_INVALID = "Invalid input parameter (%s).";
	private static final String REASON_EXCEPTION = "Core Bank is Exception : Direct Debit";
	private static final String RESULT_KEY = "RESULT";
	private static final String RESULT_PARAMS = "PARAMS";
	private static final String TRANSACTION_NO = "TRANSACTIONNO";
	private static final String REASON_CODE_104 = "104";
	private static final String EXCEPTION_STATUS = "AC";
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("en"));
	private SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("en"));

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private MasterMappingCodeRepository masterMappingCodeRepository;

	@Autowired
	private LogTransactionRepository logTransactionRepository;

	@Autowired
	private AccountServiceImpl accountServiceImpl;

	@Autowired
	private BankCaseRepository bankCaseRepository;

	@Autowired
	private MappingExceptionRepository mappingExceptionRepository;

	@Autowired
	private GeneratorCoreBankServiceImpl generatorCoreBankServiceImpl;

}