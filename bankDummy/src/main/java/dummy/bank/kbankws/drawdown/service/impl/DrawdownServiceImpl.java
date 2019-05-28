package gec.scf.dummy.bank.kbankws.drawdown.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import gec.scf.dummy.common.utils.SecurityUtils;
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
import gec.scf.dummy.bank.kbankws.core.config.DelayProcess;
import gec.scf.dummy.bank.kbankws.credit.limit.service.impl.EnquiryCreditLimitServiceImpl;
import gec.scf.dummy.bank.kbankws.debit.domain.TransactionStatus;
import gec.scf.dummy.bank.kbankws.drawdown.domain.DrawdownRequest;
import gec.scf.dummy.bank.kbankws.drawdown.domain.DrawdownResponse;
import gec.scf.dummy.bank.kbankws.drawdown.service.DrawdownService;
import gec.scf.dummy.bank.kbankws.enums.AccountStatus;
import gec.scf.dummy.bank.kbankws.enums.AccountType;
import gec.scf.dummy.bank.kbankws.enums.BankCaseEnums;
import gec.scf.dummy.bank.kbankws.enums.LogType;
import gec.scf.dummy.bank.kbankws.enums.ServiceType;
import gec.scf.dummy.bank.kbankws.generator.service.impl.GeneratorCoreBankServiceImpl;
import gec.scf.dummy.bank.repository.account.RegisterAccountRepository;
import gec.scf.dummy.bank.repository.bankcase.BankCaseRepository;
import gec.scf.dummy.bank.repository.logtransaction.LogTransactionRepository;
import gec.scf.dummy.bank.repository.mappingcode.MappingCodeRepository;
import gec.scf.dummy.bank.repository.mappingexception.MappingExceptionRepository;
import io.seruco.encoding.base62.Base62;

@Profile(Constants.Profiles.PRODUCTION)
@Service
public class DrawdownServiceImpl implements DrawdownService {

	@Override
	public Optional<DrawdownResponse> createDrawdown(String bankCode, DrawdownRequest drawdownRequest) {
		MasterBankAccount findSourceAccNo = new MasterBankAccount();
		MasterBankAccount findDestinationAccNo = new MasterBankAccount();
		MasterBankAccount findSponsorCreditLimitAccNo = new MasterBankAccount();
		DrawdownResponse drawdownResponse = new DrawdownResponse();
		MasterMappingCode masterMappingCode = new MasterMappingCode();
		MasterMappingResponseException mappingResponseException = new MasterMappingResponseException();
		Map<String, MasterMappingCodeDetail> mappingCodeDetailDefaults = new HashMap<>();
		Map<String, MasterMappingCodeDetail> mappingCodeDetails = new HashMap<>();
		boolean isWriteLogTransaction = true;
		String bank_code_encode = "";

		try {
			// Step 1 : Check Request Parameter Not Null
			Map<String, Object> resultValidate = isInvalidInputParameter(bankCode, drawdownRequest);
			boolean isInValid = (Boolean) resultValidate.get(RESULT_KEY);
			if (isInValid) {
				isWriteLogTransaction = false;
				return Optional.of(invalidInputParameterResponse(drawdownResponse, resultValidate));
			}

			// Decode parameter
			Base62 base62 = Base62.createInstance();
			final byte[] decoded = base62.decode(bankCode.getBytes());
			bank_code_encode = new String(decoded);

			// Step 2: Check MasterMapping Response Exception
			final String serviceType = ServiceType.DRAWDOWN.getCode();
			mappingResponseException = mappingExceptionRepository.findMappingExceptionByBankCodeAccNoServiceType(
					bank_code_encode, drawdownRequest.getSourceAccountNo(), serviceType, EXCEPTION_STATUS);
			if (mappingResponseException != null) {
				isWriteLogTransaction = false;
				int delay = mappingResponseException.getDelay();
				DelayProcess.delay(delay);
				drawdownResponse.setTransactionNo(drawdownRequest.getTransactionNo());
				drawdownResponse.setDrawdownTime(simpleDateTimeFormat.format(new Date()));
				drawdownResponse.setTransactionStatus(
						prepareTransactionStatus(mappingResponseException.getTransactionStatus()));
				drawdownResponse.setFailureReason(mappingResponseException.getFailureReason());
				drawdownResponse.setFailureReasonCode(mappingResponseException.getFailureReasonCode());
				return Optional.of(drawdownResponse);
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

			// Step 4 : Check Duplicate Transaction
			if (isDuplicateTransactionNo(drawdownRequest)) {
				// MappingCode - Case Fail Duplicate TransactionNo
				isWriteLogTransaction = false;
				MasterMappingCodeDetail mappingCodeDetailCaseDuplicateTransactionNo = mappingCodeDetails
						.get(BankCaseEnums.DRAWDOWN_FAILED_DUPLICATE_TRANSACTION.getCode());
				if (mappingCodeDetailCaseDuplicateTransactionNo == null) {
					mappingCodeDetailCaseDuplicateTransactionNo = mappingCodeDetailDefaults
							.get(BankCaseEnums.DRAWDOWN_FAILED_DUPLICATE_TRANSACTION.getCode());
				}
				// Step 12 : Return Response
				return Optional.of(prepareDrawdownResponse(bank_code_encode, drawdownRequest, drawdownResponse,
						mappingCodeDetailCaseDuplicateTransactionNo, null));
			}
			DelayProcess.delay(masterMappingCode.getDelay());

			// Step 5 : Check Bank Account of sponsor

			findSponsorCreditLimitAccNo = accountRepository
					.findMasterBankAccountBySponsorCode(drawdownRequest.getSponsorCode(), Boolean.TRUE,drawdownRequest.getSourceAccountNo());
			if (findSponsorCreditLimitAccNo == null) {
				// MappingCode - Case Fail Account Not Register
				MasterMappingCodeDetail mappingCodeDetailCaseSponsorCreditLimitAccountNotRegister = mappingCodeDetails
						.get(BankCaseEnums.DRAWDOWN_FAILED_SPONSORCREDITLIMIT_NOT_REGISTER.getCode());
				if (mappingCodeDetailCaseSponsorCreditLimitAccountNotRegister == null) {
					mappingCodeDetailCaseSponsorCreditLimitAccountNotRegister = mappingCodeDetailDefaults
							.get(BankCaseEnums.DRAWDOWN_FAILED_SPONSORCREDITLIMIT_NOT_REGISTER.getCode());
				}
				// Step 12 : Return Response
				return Optional.of(prepareDrawdownResponse(bank_code_encode, drawdownRequest, drawdownResponse,
						mappingCodeDetailCaseSponsorCreditLimitAccountNotRegister, findSponsorCreditLimitAccNo));
			}

			// Step 7 : Check Account Status is Suspend
			if (AccountStatus.SUSPEND.getCode().equals(findSponsorCreditLimitAccNo.getAccountStatus())) {
				MasterMappingCodeDetail mappingCodeDetailCaseSponsorAccountStatusNotActive = mappingCodeDetails
						.get(BankCaseEnums.DRAWDOWN_FAILED_SPONSORACCSTATUS_NOT_ACTIVE.getCode());
				if (mappingCodeDetailCaseSponsorAccountStatusNotActive == null) {
					mappingCodeDetailCaseSponsorAccountStatusNotActive = mappingCodeDetailDefaults
							.get(BankCaseEnums.DRAWDOWN_FAILED_SPONSORACCSTATUS_NOT_ACTIVE.getCode());
				}
				// Step 12 : Return Response
				return Optional.of(prepareDrawdownResponse(bank_code_encode, drawdownRequest, drawdownResponse,
						mappingCodeDetailCaseSponsorAccountStatusNotActive, findSponsorCreditLimitAccNo));
			}

			// Step 5 : Check Bank Account of source account no
			findSourceAccNo = accountRepository.findMasterBankAccountByAccountNoBankCodeAndAccountType(
					drawdownRequest.getSourceAccountNo(), bank_code_encode, AccountType.TERM_LOAN.getCode());
			if (findSourceAccNo == null) {
				// MappingCode - Case Fail Account Not Register
				MasterMappingCodeDetail mappingCodeDetailCaseAccountNotRegister = mappingCodeDetails
						.get(BankCaseEnums.DRAWDOWN_FAILED_SOURCEACC_NOT_REGISTER.getCode());
				if (mappingCodeDetailCaseAccountNotRegister == null) {
					mappingCodeDetailCaseAccountNotRegister = mappingCodeDetailDefaults
							.get(BankCaseEnums.DRAWDOWN_FAILED_SOURCEACC_NOT_REGISTER.getCode());
				}
				// Step 12 : Return Response
				return Optional.of(prepareDrawdownResponse(bank_code_encode, drawdownRequest, drawdownResponse,
						mappingCodeDetailCaseAccountNotRegister, findSponsorCreditLimitAccNo));
			}

			// Step 7 : Check Account Status is Suspend
			if (AccountStatus.SUSPEND.getCode().equals(findSourceAccNo.getAccountStatus())) {
				MasterMappingCodeDetail mappingCodeDetailCaseSourceAccountStatusNotActive = mappingCodeDetails
						.get(BankCaseEnums.DRAWDOWN_FAILED_SOURCEACCSTATUS_NOT_ACTIVE.getCode());
				if (mappingCodeDetailCaseSourceAccountStatusNotActive == null) {
					mappingCodeDetailCaseSourceAccountStatusNotActive = mappingCodeDetailDefaults
							.get(BankCaseEnums.DRAWDOWN_FAILED_SOURCEACCSTATUS_NOT_ACTIVE.getCode());
				}
				// Step 12 : Return Response
				return Optional.of(prepareDrawdownResponse(bank_code_encode, drawdownRequest, drawdownResponse,
						mappingCodeDetailCaseSourceAccountStatusNotActive, findSponsorCreditLimitAccNo));
			}

			// Step 6 : Check Bank Account of destination account
			findDestinationAccNo = accountRepository.findMasterBankAccountByAccountNoAndBankCode(
					drawdownRequest.getDestinationAccountNo(), bank_code_encode);
			if (findDestinationAccNo == null) {
				// MappingCode - Case Fail Account Not Register
				MasterMappingCodeDetail mappingCodeDetailCaseDestAccountNotRegister = mappingCodeDetails
						.get(BankCaseEnums.DRAWDOWN_FAILED_DESTACC_NOT_REGISTER.getCode());
				if (mappingCodeDetailCaseDestAccountNotRegister == null) {
					mappingCodeDetailCaseDestAccountNotRegister = mappingCodeDetailDefaults
							.get(BankCaseEnums.DRAWDOWN_FAILED_DESTACC_NOT_REGISTER.getCode());
				}
				return Optional.of(prepareDrawdownResponse(bank_code_encode, drawdownRequest, drawdownResponse,
						mappingCodeDetailCaseDestAccountNotRegister, findSponsorCreditLimitAccNo));
			}

			// Step 7 : Check Account Status is Suspend
			if (AccountStatus.SUSPEND.getCode().equals(findDestinationAccNo.getAccountStatus())) {
				MasterMappingCodeDetail mappingCodeDetailCaseDestAccountStatusNotActive = mappingCodeDetails
						.get(BankCaseEnums.DRAWDOWN_FAILED_DESTACCSTATUS_NOT_ACTIVE.getCode());
				if (mappingCodeDetailCaseDestAccountStatusNotActive == null) {
					mappingCodeDetailCaseDestAccountStatusNotActive = mappingCodeDetailDefaults
							.get(BankCaseEnums.DRAWDOWN_FAILED_DESTACCSTATUS_NOT_ACTIVE.getCode());
				}
				// Step 12 : Return Response
				return Optional.of(prepareDrawdownResponse(bank_code_encode, drawdownRequest, drawdownResponse,
						mappingCodeDetailCaseDestAccountStatusNotActive, findSponsorCreditLimitAccNo));
			}

			// Step 8 : Calculate Transaction amount
			BigDecimal newTransactionAmount = calculateTransactionAmount(drawdownRequest);

			// Step : Check Transaction amount of source account no and CreditLimit of
			// sponsor
			if (findSponsorCreditLimitAccNo.getAvailable().compareTo(newTransactionAmount) < 0) {
				MasterMappingCodeDetail mappingCodeDetailCaseInsufficientCreditLimit = mappingCodeDetails
						.get(BankCaseEnums.DRAWDOWN_FAILED_INSUFFICIENT_SPONSORCREDITLIMIT.getCode());
				if (mappingCodeDetailCaseInsufficientCreditLimit == null) {
					mappingCodeDetailCaseInsufficientCreditLimit = mappingCodeDetailDefaults
							.get(BankCaseEnums.DRAWDOWN_FAILED_INSUFFICIENT_SPONSORCREDITLIMIT.getCode());
				}
				// Step 12 : Return Response
				return Optional.of(prepareDrawdownResponse(bank_code_encode, drawdownRequest, drawdownResponse,
						mappingCodeDetailCaseInsufficientCreditLimit, findSponsorCreditLimitAccNo));
			}

			// Step 9 : Check Available of source account no
			if (findSourceAccNo.getAvailable().compareTo(newTransactionAmount) < 0) {
				MasterMappingCodeDetail mappingCodeDetailCaseInsufficientFund = mappingCodeDetails
						.get(BankCaseEnums.DRAWDOWN_FAILED_INSUFFICIENT_FUND.getCode());
				if (mappingCodeDetailCaseInsufficientFund == null) {
					mappingCodeDetailCaseInsufficientFund = mappingCodeDetailDefaults
							.get(BankCaseEnums.DRAWDOWN_FAILED_INSUFFICIENT_FUND.getCode());
				}
				// Step 12 : Return Response
				return Optional.of(prepareDrawdownResponse(bank_code_encode, drawdownRequest, drawdownResponse,
						mappingCodeDetailCaseInsufficientFund, findSponsorCreditLimitAccNo));
			}

			// Step 10 : Check OutStanding of Destination Account
			if (AccountType.TERM_LOAN.getCode().equals(findDestinationAccNo.getAccountType())
					&& findDestinationAccNo.getOutstanding().compareTo(newTransactionAmount) < 0) {
				MasterMappingCodeDetail mappingCodeDetailCaseCannotTransferFunds = mappingCodeDetails
						.get(BankCaseEnums.DRAWDOWN_FAILED_CANNOT_TRANSFER.getCode());
				if (mappingCodeDetailCaseCannotTransferFunds == null) {
					mappingCodeDetailCaseCannotTransferFunds = mappingCodeDetailDefaults
							.get(BankCaseEnums.DRAWDOWN_FAILED_CANNOT_TRANSFER.getCode());
				}
				// Step 12 : Return Response
				return Optional.of(prepareDrawdownResponse(bank_code_encode, drawdownRequest, drawdownResponse,
						mappingCodeDetailCaseCannotTransferFunds, findSponsorCreditLimitAccNo));
			}
			// Step 11 : Process Transfer
			processTransfer(findSourceAccNo, findDestinationAccNo, findSponsorCreditLimitAccNo, newTransactionAmount);
			MasterMappingCodeDetail mappingCodeDetailCaseTransferSuccess = mappingCodeDetails
					.get(BankCaseEnums.DRAWDOWN_SUCCESS.getCode());
			if (mappingCodeDetailCaseTransferSuccess == null) {
				mappingCodeDetailCaseTransferSuccess = mappingCodeDetailDefaults
						.get(BankCaseEnums.DRAWDOWN_SUCCESS.getCode());
			}
			// Step 12 : Return Response
			return Optional.of(prepareDrawdownResponse(bank_code_encode, drawdownRequest, drawdownResponse,
					mappingCodeDetailCaseTransferSuccess, findSponsorCreditLimitAccNo));

		} catch (Exception e) {
			drawdownResponse.setTransactionNo(drawdownRequest.getTransactionNo());
			drawdownResponse.setTransactionStatus(TransactionStatus.ERROR);
			drawdownResponse.setFailureReasonCode(REASON_CODE_E99);
			drawdownResponse.setFailureReason(REASON_EXCEPTION);
			return Optional.of(drawdownResponse);
		} finally {
			// Step 13 : LogTransaction
			try {
				if (isWriteLogTransaction) {
					// Write Log Transaction
					saveLogTransaction(drawdownResponse,findSponsorCreditLimitAccNo , findSourceAccNo, findDestinationAccNo, drawdownRequest,bank_code_encode);
				}
			} catch (Exception e2) {
			}
		}
	}

	@Override
	public Optional<DrawdownResponse> getDrawdown(String bankCode, DrawdownRequest drawdownRequest) {
		DrawdownResponse drawdownResponse = new DrawdownResponse();
		boolean isWriteLogTransaction = true;
		MasterMappingResponseException mappingResponseException = new MasterMappingResponseException();
		LogTransaction findLogtransaction = null;
		String bank_code_encode = "";
		boolean isEnquiryDrawdownSuccess = false;
		try {
			// Step 1 : Check Invalid TransactionNo
			Map<String, Object> resultValidate = isInvalidDrawdownRequest(bankCode, drawdownRequest);
			boolean isInValid = (Boolean) resultValidate.get(RESULT_KEY);
			if (isInValid) {
				isWriteLogTransaction = false;
				String resultParams = (String) resultValidate.get(RESULT_PARAMS);
				drawdownResponse.setTransactionNo(drawdownRequest.getTransactionNo());
				drawdownResponse.setDrawdownTime(simpleDateTimeFormat.format(new Date()));
				drawdownResponse.setTransactionStatus(TransactionStatus.FAILED);
				drawdownResponse.setFailureReasonCode(REASON_CODE_E01);
				drawdownResponse.setFailureReason(String.format(REASON_ACC_INVALID, resultParams));
				// Step 3 : Return Response
				return Optional.of(drawdownResponse);
			}
			// Decode Parameter
			Base62 base62 = Base62.createInstance();
			final byte[] bank_code_decoded = base62.decode(bankCode.getBytes());
			bank_code_encode = new String(bank_code_decoded);
			final byte[] transaction_no_decoded = base62.decode(drawdownRequest.getTransactionNo().getBytes());
			String transaction_no_encode = new String(transaction_no_decoded);
			
			// Step 2 : Transaction No is not found
			findLogtransaction = logTransactionRepository
					.findLogTransactionByTransactionNoAndBankCode(transaction_no_encode, LogType.DRAWDOWN.getCode());
			if (findLogtransaction == null) {
				isWriteLogTransaction = false;
				drawdownResponse.setTransactionNo(transaction_no_encode);
				drawdownResponse.setDrawdownTime(simpleDateTimeFormat.format(new Date()));
				drawdownResponse.setTransactionStatus(TransactionStatus.FAILED);
				drawdownResponse.setFailureReasonCode(REASON_CODE_104);
				drawdownResponse.setFailureReason(String.format(REASON_ACC_INVALID, "Transaction No is not found"));
				// Step 3 : Return Response
				return Optional.of(drawdownResponse);
			}
			final String serviceType = ServiceType.ENQUIRY_DRAWDOWN.getCode();
			mappingResponseException = mappingExceptionRepository.findMappingExceptionByBankCodeAccNoServiceType(
					bank_code_encode, findLogtransaction.getSourceAccountNo(), serviceType, EXCEPTION_STATUS);
			if (mappingResponseException != null) {
				isWriteLogTransaction = false;
				int delay = mappingResponseException.getDelay();
				DelayProcess.delay(delay);
				drawdownResponse.setTransactionNo(transaction_no_encode);
				drawdownResponse.setDrawdownTime(simpleDateTimeFormat.format(new Date()));
				drawdownResponse.setTransactionStatus(
						prepareTransactionStatus(mappingResponseException.getTransactionStatus()));
				drawdownResponse.setFailureReason(mappingResponseException.getFailureReason());
				drawdownResponse.setFailureReasonCode(mappingResponseException.getFailureReasonCode());
				return Optional.of(drawdownResponse);
			}
			
			// Step 3 : Return Response
			isEnquiryDrawdownSuccess = true;
			return Optional
					.of(prepareEnquiryDrawdownResponse(drawdownResponse, findLogtransaction, transaction_no_encode));
		} catch (Exception e) {
			// Step 3 : Return Response
			drawdownResponse.setTransactionNo(drawdownRequest.getTransactionNo());
			drawdownResponse.setDrawdownTime(simpleDateTimeFormat.format(new Date()));
			drawdownResponse.setTransactionStatus(TransactionStatus.ERROR);
			drawdownResponse.setFailureReasonCode(REASON_CODE_E99);
			drawdownResponse.setFailureReason(String.format(REASON_EXCEPTION, "Enquiry Drawdown."));
			return Optional.of(drawdownResponse);
		} finally {
			// Step 4 : LogTransaction
			try {
				if (isWriteLogTransaction) {
					// Write Log Transaction
					SaveLogTransactionEnquiryDrawdown(drawdownResponse, findLogtransaction, isEnquiryDrawdownSuccess);
				}
			} catch (Exception e) {

			}
		}
	}

	private DrawdownResponse invalidInputParameterResponse(DrawdownResponse drawdownResponse,
			Map<String, Object> resultValidate) {
		String resultParams = (String) resultValidate.get(RESULT_PARAMS);
		String resultTransactionNo = (String) resultValidate.get(TRANSACTION_NO);
		drawdownResponse.setTransactionNo(resultTransactionNo);
		drawdownResponse.setTransactionStatus(TransactionStatus.FAILED);
		drawdownResponse.setFailureReasonCode(REASON_CODE_E01);
		drawdownResponse.setFailureReason(String.format(REASON_ACC_INVALID, resultParams));
		return drawdownResponse;
	}

	private void saveLogTransaction(DrawdownResponse drawdownResponse, MasterBankAccount findSponsorCreditLimitAccNo,MasterBankAccount findSourceAccNo,
			MasterBankAccount findDestinationAccNo, DrawdownRequest drawdownRequest,String bankCode) {
		try {
			LogTransaction log = new LogTransaction();
			log.setLogType(LogType.DRAWDOWN.getCode());
			log.setTransactionNo(drawdownRequest.getTransactionNo());
			log.setBankTransactionNo(drawdownResponse.getBankTransactionNo());
			log.setBankCode(bankCode);
			
			if (findSponsorCreditLimitAccNo !=  null) {
				log.setSponsorCode(findSponsorCreditLimitAccNo.getCustomerCode());
				log.setSponsorCreditAccountNo(findSponsorCreditLimitAccNo.getAccountNo());
				log.setSponsorCreditLimitAccountType(findSponsorCreditLimitAccNo.getAccountType());
				log.setSponsorCreditLimitCustomerID(findSponsorCreditLimitAccNo.getCustomerID());
			} else {
				log.setSponsorCode(drawdownRequest.getSponsorCode());
			}
			if (findSourceAccNo != null) {
				log.setSourceAccountNo(findSourceAccNo.getAccountNo());
				log.setSourceAccountType(findSourceAccNo.getAccountType());
				log.setSourceCustomerID(findSourceAccNo.getCustomerID());
			} else {
				log.setSourceAccountNo(drawdownRequest.getSourceAccountNo());
			}
			if (findDestinationAccNo != null) {
				log.setDestinationCustomerID(findDestinationAccNo.getCustomerID());
				log.setDestinationAccountNo(findDestinationAccNo.getAccountNo());
				log.setDestinationAccountType(findDestinationAccNo.getAccountType());
			} else {
				log.setDestinationAccountNo(drawdownRequest.getDestinationAccountNo());
			}

			log.setTransactionDate(simpleDateFormat.parse(drawdownRequest.getTransactionDate()));
			log.setTransactionStatus(drawdownResponse.getTransactionStatus().getCode());
			log.setTransactionAmount(drawdownRequest.getTransactionAmount());
			log.setCurrencyCode(drawdownRequest.getCurrencyCode());
			log.setFailureReason(drawdownResponse.getFailureReason());
			log.setFailureReasonCode(drawdownResponse.getFailureReasonCode());
			log.setMaturityDate(drawdownRequest.getMaturityDate());
			log.setDocumentAmount(drawdownRequest.getDocumentAmount());
			log.setFinancePercent(drawdownRequest.getFinancePercent());
			Date currentTime = new Date();
			log.setUpdateTime(currentTime);
			logTransactionRepository.save(log);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void SaveLogTransactionEnquiryDrawdown(DrawdownResponse drawdownResponse,
			LogTransaction findLogtransaction, boolean isEnquiryDrawdownSuccess) throws Exception {
		try {
			LogTransaction log = new LogTransaction();
			log.setTransactionNo(drawdownResponse.getTransactionNo());
			log.setLogType(LogType.ENQUIRY_DRAWDOWN.getCode());
			log.setSourceAccountNo(findLogtransaction.getSourceAccountNo());
			log.setBankCode(findLogtransaction.getBankCode());
			log.setSourceCustomerID(findLogtransaction.getSourceCustomerID());
			log.setSourceAccountType(findLogtransaction.getSourceAccountType());
			log.setBankTransactionNo(findLogtransaction.getBankTransactionNo());
			log.setDestinationAccountNo(findLogtransaction.getDestinationAccountNo());
			log.setDestinationCustomerID(findLogtransaction.getDestinationCustomerID());
			log.setDestinationAccountType(findLogtransaction.getDestinationAccountType());
			
			log.setSponsorCode(findLogtransaction.getSponsorCode());
			log.setSponsorCreditLimitCustomerID(findLogtransaction.getSponsorCreditLimitCustomerID());
			log.setSponsorCreditLimitAccountType(findLogtransaction.getSponsorCreditLimitAccountType());
			log.setSponsorCreditAccountNo(findLogtransaction.getSponsorCreditAccountNo());

			log.setTransactionDate(findLogtransaction.getTransactionDate());
			log.setTransactionAmount(findLogtransaction.getTransactionAmount());
			log.setCurrencyCode(findLogtransaction.getCurrencyCode());
			
			log.setMaturityDate(findLogtransaction.getMaturityDate());
			log.setDocumentAmount(findLogtransaction.getDocumentAmount());
			log.setFinancePercent(findLogtransaction.getFinancePercent());
			
			if(isEnquiryDrawdownSuccess) {
				log.setTransactionStatus(TransactionStatus.SUCCESS.getCode());
				log.setFailureReasonCode(null);
				log.setFailureReason("Enquiry Drawdown Success");
			}else {
				log.setTransactionStatus(drawdownResponse.getTransactionStatus().toString());
				log.setFailureReasonCode(drawdownResponse.getFailureReason());
				log.setFailureReason(drawdownResponse.getFailureReason());
			}
			
			Date currentTime = new Date();
			log.setUpdateTime(currentTime);
			logTransactionRepository.save(log);
			
		} catch (Exception e) {
			throw e;
		}
	}


	private void processTransfer(MasterBankAccount findSourceAccNo, MasterBankAccount findDestinationAccNo,
			MasterBankAccount findSponsorCreditLimitAccNo, BigDecimal transactionAmount) {
		try {
			Date updateTime = new Date();
			MasterBankAccount destinationAccNo = updateDestinationAccBalance(findDestinationAccNo, transactionAmount);
			destinationAccNo.setUpdateTime(updateTime);

			MasterBankAccount sponsorAccNo = updateSponsorBalance(findSponsorCreditLimitAccNo, transactionAmount);
			sponsorAccNo.setUpdateTime(updateTime);

			MasterBankAccount sourceAccNo = updateSourceAccBalance(findSourceAccNo, transactionAmount);
			sourceAccNo.setUpdateTime(updateTime);

			accountRepository.save(sourceAccNo);
			accountRepository.save(destinationAccNo);
			accountRepository.save(sponsorAccNo);
		} catch (Exception e) {
			throw e;
		}
	}

	private MasterBankAccount updateSponsorBalance(MasterBankAccount findSponsorCreditLimitAccNo,
			BigDecimal transactionAmount) {
		try {
			Date updateTime = new Date();
			BigDecimal sponsorOutstandingAmt = findSponsorCreditLimitAccNo.getOutstanding().add(transactionAmount);
			BigDecimal sponsorAvailableAmt = findSponsorCreditLimitAccNo.getCreditLimit()
					.subtract(sponsorOutstandingAmt);

			if (AccountType.OVER_DRAFT.getCode().equals(findSponsorCreditLimitAccNo.getAccountType())) {
				BigDecimal sponsorLedgerBalanceAmt = findSponsorCreditLimitAccNo.getLedgerBalance()
						.subtract(transactionAmount);
				findSponsorCreditLimitAccNo.setLedgerBalance(sponsorLedgerBalanceAmt);
			}
			findSponsorCreditLimitAccNo.setOutstanding(sponsorOutstandingAmt);
			findSponsorCreditLimitAccNo.setAvailable(sponsorAvailableAmt);
			findSponsorCreditLimitAccNo.setUpdateBy(SecurityUtils.getUser().getId());
			findSponsorCreditLimitAccNo.setUpdateTime(updateTime);
		} catch (Exception e) {
			throw e;
		}
		return findSponsorCreditLimitAccNo;
	}

	private MasterBankAccount updateSourceAccBalance(MasterBankAccount findSourceAccNo, BigDecimal transactionAmount) {
		try {
			Date updateTime = new Date();
			BigDecimal sourceOutstandingAmt = findSourceAccNo.getOutstanding().add(transactionAmount);
			BigDecimal sourceAvailableAmt = findSourceAccNo.getCreditLimit().subtract(sourceOutstandingAmt);
			findSourceAccNo.setOutstanding(sourceOutstandingAmt);
			findSourceAccNo.setAvailable(sourceAvailableAmt);
			findSourceAccNo.setUpdateBy(SecurityUtils.getUser().getId());
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
			BigDecimal destinationAvailableAmt = findDestinationAccNo.getAvailable();
			BigDecimal destinationLedgerBalanceAmt = findDestinationAccNo.getLedgerBalance();
			BigDecimal destinationOutstandingAmt = findDestinationAccNo.getOutstanding();
			BigDecimal destinationCreditLimitAmt = findDestinationAccNo.getCreditLimit();

			if (AccountType.TERM_LOAN.getCode().equals(findDestinationAccNo.getAccountType())) {
				destinationOutstandingAmt = destinationOutstandingAmt.subtract(transactionAmount);
				destinationAvailableAmt = destinationCreditLimitAmt.subtract(destinationOutstandingAmt);
			}
			if (AccountType.CURRENT.getCode().equals(findDestinationAccNo.getAccountType())
					|| AccountType.SAVING.getCode().equals(findDestinationAccNo.getAccountType())) {
				destinationLedgerBalanceAmt = destinationLedgerBalanceAmt.add(transactionAmount);
				destinationAvailableAmt = destinationLedgerBalanceAmt;
			}
			if (AccountType.OVER_DRAFT.getCode().equals(findDestinationAccNo.getAccountType())) {
				destinationLedgerBalanceAmt = destinationLedgerBalanceAmt.add(transactionAmount);
				destinationOutstandingAmt = destinationLedgerBalanceAmt.multiply(new BigDecimal("-1.00"));
				destinationAvailableAmt = destinationCreditLimitAmt.add(destinationLedgerBalanceAmt);
				if (destinationLedgerBalanceAmt.compareTo(new BigDecimal("0.00")) > 0) {
					destinationOutstandingAmt = new BigDecimal("0.00");
				}
			}
			findDestinationAccNo.setCreditLimit(destinationCreditLimitAmt);
			findDestinationAccNo.setOutstanding(destinationOutstandingAmt);
			findDestinationAccNo.setLedgerBalance(destinationLedgerBalanceAmt);
			findDestinationAccNo.setAvailable(destinationAvailableAmt);
			findDestinationAccNo.setUpdateBy(SecurityUtils.getUser().getId());
			findDestinationAccNo.setUpdateTime(updateTime);

		} catch (Exception e) {
			throw e;
		}
		return findDestinationAccNo;
	}

	private BigDecimal calculateTransactionAmount(DrawdownRequest drawdownRequest) {
		BigDecimal transactionAmount = new BigDecimal("0.00");
		try {
			BigDecimal documentAmount = new BigDecimal(drawdownRequest.getDocumentAmount());
			BigDecimal financePercent = new BigDecimal(drawdownRequest.getFinancePercent());
			transactionAmount = ((documentAmount.multiply(financePercent)).divide(new BigDecimal("100")));
			drawdownRequest.setTransactionAmount(transactionAmount.toString());
		} catch (Exception e) {
			throw e;
		}
		return transactionAmount;
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
					.findBankCaseByBankCaseGroup(ServiceType.DRAWDOWN.getCode());
			if (!bankCase.isEmpty()) {
				for (MasterBankCase each : bankCase) {
					masterMappingCodeDetail.add(new MasterMappingCodeDetail(3L, each.getBankCaseCode(),
							each.getBankCaseStatus(), each.getDefaultReasonCode(), each.getBankCaseName()));
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return masterMappingCodeDetail;
	}

	private boolean isDuplicateTransactionNo(DrawdownRequest drawdownRequest) {
		boolean result = false;
		try {
			LogTransaction findTransactionNo = logTransactionRepository
					.findLogTransactionByTransactionNo(drawdownRequest.getTransactionNo(), LogType.DRAWDOWN.getCode());
			if (findTransactionNo != null) {
				result = true;
			}
		} catch (Exception e) {
			return result;
		}
		return result;
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

	private DrawdownResponse prepareEnquiryDrawdownResponse(DrawdownResponse drawdownResponse,
			LogTransaction findLogtransaction, String transactionNo) {
		if (TransactionStatus.SUCCESS.getCode().equals(findLogtransaction.getTransactionStatus())) {
			drawdownResponse.setTransactionNo(transactionNo);
			drawdownResponse.setDrawdownTime(simpleDateTimeFormat.format(new Date()));
			drawdownResponse.setBankTransactionNo(findLogtransaction.getBankTransactionNo());
			drawdownResponse.setTransactionStatus(prepareTransactionStatus(findLogtransaction.getTransactionStatus()));
			drawdownResponse.setFailureReasonCode(findLogtransaction.getFailureReasonCode());
			drawdownResponse.setFailureReason(findLogtransaction.getFailureReason());
		} else {
			drawdownResponse.setTransactionNo(transactionNo);
			drawdownResponse.setDrawdownTime(simpleDateTimeFormat.format(new Date()));
			drawdownResponse.setTransactionStatus(prepareTransactionStatus(findLogtransaction.getTransactionStatus()));
			drawdownResponse.setFailureReasonCode(findLogtransaction.getFailureReasonCode());
			drawdownResponse.setFailureReason(findLogtransaction.getFailureReason());
		}
		return drawdownResponse;
	}

	private DrawdownResponse prepareDrawdownResponse(String bankCode, DrawdownRequest drawdownRequest,
			DrawdownResponse drawdownResponse, MasterMappingCodeDetail mappingCodeDetail,
			MasterBankAccount findSponsorCreditLimitAccNo) {
		drawdownResponse.setTransactionNo(drawdownRequest.getTransactionNo());
		if (TransactionStatus.SUCCESS.getCode().equals(mappingCodeDetail.getTransactionStatus())) {
			mappingCodeDetail.setFailureReasonCode(mappingCodeDetail.getFailureReasonCode());
			mappingCodeDetail.setFailureReason(mappingCodeDetail.getFailureReason());
			drawdownResponse.setBankTransactionNo(generatorCode(ServiceType.DRAWDOWN));
			drawdownResponse.setDrawdownTime(simpleDateTimeFormat.format(new Date()));
			drawdownResponse.setSourceAccount(getEnquiryCreditLimit(bankCode, drawdownRequest));
			drawdownResponse
					.setSponsorCreditLimitAccount(getEnquiryCreditLimitSponsor(bankCode, findSponsorCreditLimitAccNo));
			drawdownResponse.setTransactionStatus(prepareTransactionStatus(mappingCodeDetail.getTransactionStatus()));
			drawdownResponse.setFailureReason(mappingCodeDetail.getFailureReason());
			drawdownResponse.setFailureReasonCode(mappingCodeDetail.getFailureReasonCode());
		} else {
			drawdownResponse.setSourceAccount(responseSourceAccNo(bankCode, drawdownRequest));
			drawdownResponse.setTransactionStatus(prepareTransactionStatus(mappingCodeDetail.getTransactionStatus()));
			drawdownResponse.setDrawdownTime(simpleDateTimeFormat.format(new Date()));
			drawdownResponse.setFailureReason(mappingCodeDetail.getFailureReason());
			drawdownResponse.setFailureReasonCode(mappingCodeDetail.getFailureReasonCode());
		}
		return drawdownResponse;
	}

	private AccountBalanceResponse responseSourceAccNo(String bankCode, DrawdownRequest drawdownRequest) {
		AccountBalanceResponse responseSourceAccNo = null;
		Base62 base62 = Base62.createInstance();
		final byte[] en_account = base62.encode(drawdownRequest.getSourceAccountNo().getBytes());
		String account_no_decode = new String(en_account);
		final byte[] de_bank_code = base62.encode(bankCode.getBytes());
		String bank_code_encode = new String(de_bank_code);
		try {
			Optional<AccountBalanceResponse> enquiryCreditLimit = enquiryCreditLimitServiceImpl
					.getCreditLimitAccount(bank_code_encode, account_no_decode);
			if (enquiryCreditLimit.isPresent()) {
				responseSourceAccNo = enquiryCreditLimit.get();
				responseSourceAccNo.setLatestTransactionTime(null);
				responseSourceAccNo.setFailureReason(null);
				responseSourceAccNo.setFailureReasonCode(null);
				responseSourceAccNo.setEnquiryStatus(null);
				responseSourceAccNo.setAccountBalance(null);
				responseSourceAccNo.setAccountStatus(null);
				responseSourceAccNo.setAccountType(null);
				responseSourceAccNo.setAvailableBalance(null);
				responseSourceAccNo.setCustomerId(null);
				responseSourceAccNo.setCurrencyCode(null);
				responseSourceAccNo.setCreditLimit(null);
				responseSourceAccNo.setOutstanding(null);
			}
		} catch (Exception e) {
			throw e;
		}
		return responseSourceAccNo;
	}

	private TransactionStatus prepareTransactionStatus(String transactionStatus) {
		for (TransactionStatus status : TransactionStatus.values()) {
			if (transactionStatus.equals(status.getCode())) {
				return status;
			}
		}
		return null;
	}

	private AccountBalanceResponse getEnquiryCreditLimit(String bankCode, DrawdownRequest drawdownRequest) {
		AccountBalanceResponse accountBalanceResponse = null;
		Base62 base62 = Base62.createInstance();
		final byte[] en_account = base62.encode(drawdownRequest.getSourceAccountNo().getBytes());
		String account_no_decode = new String(en_account);
		final byte[] de_bank_code = base62.encode(bankCode.getBytes());
		String bank_code_encode = new String(de_bank_code);
		try {
			Optional<AccountBalanceResponse> enquiryCreditLimit = enquiryCreditLimitServiceImpl
					.getCreditLimitAccount(bank_code_encode, account_no_decode);
			if (enquiryCreditLimit.isPresent()) {
				accountBalanceResponse = enquiryCreditLimit.get();
			}
		} catch (Exception e) {
			throw e;
		}
		return accountBalanceResponse;
	}

	private AccountBalanceResponse getEnquiryCreditLimitSponsor(String bankCode,
			MasterBankAccount findSponsorCreditLimitAccNo) {
		AccountBalanceResponse accountBalanceResponse = null;
		Base62 base62 = Base62.createInstance();
		final byte[] en_sponsor = base62.encode(findSponsorCreditLimitAccNo.getAccountNo().getBytes());
		String account_sponsor_decode = new String(en_sponsor);
		final byte[] de_bank_code = base62.encode(bankCode.getBytes());
		String bank_code_encode = new String(de_bank_code);
		try {

			Optional<AccountBalanceResponse> enquiryCreditLimitSponsor = enquiryCreditLimitServiceImpl
					.getCreditLimitAccount(bank_code_encode, account_sponsor_decode);
			if (enquiryCreditLimitSponsor.isPresent()) {
				accountBalanceResponse = enquiryCreditLimitSponsor.get();
			}
		} catch (Exception e) {
			throw e;
		}
		return accountBalanceResponse;
	}

	private Map<String, Object> isInvalidDrawdownRequest(String bankCode, DrawdownRequest drawdownRequest) {
		Map<String, Object> result = new HashMap<>();
		result.put(RESULT_KEY, false);
		result.put(RESULT_PARAMS, "");
		if (drawdownRequest == null) {
			result.put(RESULT_KEY, true);
			result.put(RESULT_PARAMS, "DrawdownRequest is NULL");
			return result;
		} else {
			if (drawdownRequest.getTransactionNo() == null || "".equals(drawdownRequest.getTransactionNo())) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "TransactionNo is NULL");
				return result;
			}
			if (drawdownRequest.getTransactionNo().length() > 50) {
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

	private Map<String, Object> isInvalidInputParameter(String bankCode, DrawdownRequest drawdownRequest) {
		Map<String, Object> result = new HashMap<>();
		result.put(RESULT_KEY, false);
		result.put(RESULT_PARAMS, "");
		result.put(TRANSACTION_NO, null);
		if (drawdownRequest == null) {
			result.put(RESULT_KEY, true);
			result.put(RESULT_PARAMS, "DrawdownRequest is NULL");
			return result;
		} else {
			if (drawdownRequest.getTransactionNo() == null || "".equals(drawdownRequest.getTransactionNo())) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "TransactionNo is NULL");
				return result;
			}
			result.put(TRANSACTION_NO, drawdownRequest.getTransactionNo());
			if (drawdownRequest.getTransactionNo().length() > 50) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "TransactionNo must no more than 50 digits.");
				return result;
			}
			if (drawdownRequest.getSponsorCode() == null || "".equals(drawdownRequest.getSponsorCode())) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "SponsorCode is NULL");
				return result;
			}
			if (drawdownRequest.getSponsorCode().length() > 20) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "SponsorCode must no more than 20 digits.");
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
			if (drawdownRequest.getSourceAccountNo() == null || "".equals(drawdownRequest.getSourceAccountNo())) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "SourceAccountNo is NULL");
				return result;
			}
			if (drawdownRequest.getSourceAccountNo().length() > 20) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "SourceAccountNo must no more than 20 digits.");
				return result;
			}
			if (drawdownRequest.getDestinationAccountNo() == null
					|| "".equals(drawdownRequest.getDestinationAccountNo())) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "DestinationAccountNo is NULL");
				return result;
			}
			if (drawdownRequest.getDestinationAccountNo().length() > 20) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "DestinationAccountNo must no more than 20 digits.");
				return result;
			}
			if (drawdownRequest.getTransactionDate() == null || "".equals(drawdownRequest.getTransactionDate())) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "TransactionDate is NULL");
				return result;
			}
			if (drawdownRequest.getTransactionDate().length() > 10) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "TransactionDate must no more than 10 digits.");
				return result;
			}
			if (isvalidateFormatDate(drawdownRequest.getTransactionDate())) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "TransactionDate is Invalid.");
				return result;
			}
			if (drawdownRequest.getMaturityDate() == null || "".equals(drawdownRequest.getMaturityDate())) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "MaturityDate is NULL");
				return result;
			}
			if (drawdownRequest.getMaturityDate().length() > 10) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "MaturityDate must no more than 10 digits.");
				return result;
			}
			if (isvalidateFormatDate(drawdownRequest.getMaturityDate())) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "MaturityDate is Invalid.");
				return result;
			}
			if (drawdownRequest.getDocumentAmount() == null || "".equals(drawdownRequest.getDocumentAmount())) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "DocumentAmount is NULL");
				return result;
			}
			if (drawdownRequest.getDocumentAmount().length() > 20) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "DocumentAmount must no more than 20 digits.");
				return result;
			}
			try {
				new BigDecimal(drawdownRequest.getDocumentAmount());
			} catch (Exception e) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "Invalid DocumentAmount Format. ");
				return result;
			}
			if (new BigDecimal(drawdownRequest.getDocumentAmount()).compareTo(BigDecimal.ZERO) <= 0) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "Invalid DocumentAmount Format  must be greater than 0. ");
				return result;
			}
			if (drawdownRequest.getFinancePercent() == null || "".equals(drawdownRequest.getFinancePercent())) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "FinancePercent  is NULL");
				return result;
			}
			if (drawdownRequest.getFinancePercent().length() > 20) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "FinancePercent must no more than 20 digits.");
				return result;
			}
			try {
				new BigDecimal(drawdownRequest.getFinancePercent());
			} catch (Exception e) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "Invalid FinancePercent Format. ");
				return result;
			}
			if (new BigDecimal(drawdownRequest.getFinancePercent()).compareTo(BigDecimal.ZERO) <= 0) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "Invalid FinancePercent Format  must be greater than 0. ");
				return result;
			}
			if (drawdownRequest.getTransactionAmount() == null || "".equals(drawdownRequest.getTransactionAmount())) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "TransactionAmount is NULL");
				return result;
			}
			if (drawdownRequest.getTransactionAmount().length() > 20) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "TransactionAmount must no more than 20 digits.");
				return result;
			}
			try {
				new BigDecimal(drawdownRequest.getTransactionAmount());
			} catch (Exception e) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "Invalid TransactionAmount Format. ");
				return result;
			}
			if (new BigDecimal(drawdownRequest.getTransactionAmount()).compareTo(BigDecimal.ZERO) <= 0) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "Invalid TransactionAmount Format  must be greater than 0. ");
				return result;
			}
			if (drawdownRequest.getCurrencyCode() == null || "".equals(drawdownRequest.getCurrencyCode())) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "CurrencyCode is NULL.");
				return result;
			}
			if (drawdownRequest.getCurrencyCode().length() > 3) {
				result.put(RESULT_KEY, true);
				result.put(RESULT_PARAMS, "CurrencyCode must no more than 3 digits.");
				return result;
			}
		}
		return result;
	}

	private boolean isvalidateFormatDate(String transactionDate) {
		try {
			simpleDateFormat.parse(transactionDate);
			return false;
		} catch (Exception e) {
			return true;
		}
	}

	private static final String REASON_CODE_E01 = "E01";
	private static final String REASON_CODE_E99 = "E99";
	private static final String REASON_CODE_104 = "104";
	private static final String REASON_ACC_INVALID = "Invalid input parameter (%s).";
	private static final String REASON_EXCEPTION = "Core Bank is Exception : Drawdown";
	private static final String RESULT_KEY = "RESULT";
	private static final String RESULT_PARAMS = "PARAMS";
	private static final String TRANSACTION_NO = "TRANSACTIONNO";
	private static final String EXCEPTION_STATUS = "AC";
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("en"));
	private SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("en"));

	@Autowired
	private RegisterAccountRepository accountRepository;

	@Autowired
	private MappingCodeRepository masterMappingCodeRepository;

	@Autowired
	private LogTransactionRepository logTransactionRepository;

	@Autowired
	private EnquiryCreditLimitServiceImpl enquiryCreditLimitServiceImpl;

	@Autowired
	private BankCaseRepository bankCaseRepository;

	@Autowired
	private MappingExceptionRepository mappingExceptionRepository;

	@Autowired
	private GeneratorCoreBankServiceImpl generatorCoreBankServiceImpl;
}
