package gec.scf.dummy.bank.kbankws.credit.limit.service.impl;

import java.text.DecimalFormat;
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
import gec.scf.dummy.bank.kbankws.core.config.DelayProcess;
import gec.scf.dummy.bank.kbankws.credit.limit.service.EnquiryCreditLimitService;
import gec.scf.dummy.bank.kbankws.enums.AccountStatus;
import gec.scf.dummy.bank.kbankws.enums.AccountType;
import gec.scf.dummy.bank.kbankws.enums.BankCaseEnums;
import gec.scf.dummy.bank.kbankws.enums.CurrencyCode;
import gec.scf.dummy.bank.kbankws.enums.LogType;
import gec.scf.dummy.bank.kbankws.enums.MappingStatus;
import gec.scf.dummy.bank.kbankws.enums.ServiceType;
import gec.scf.dummy.bank.repository.bankcase.BankCaseRepository;
import gec.scf.dummy.bank.repository.logtransaction.LogTransactionRepository;
import gec.scf.dummy.bank.repository.mappingexception.MappingExceptionRepository;
import io.seruco.encoding.base62.Base62;

@Service
@Profile(Constants.Profiles.PRODUCTION)
public class EnquiryCreditLimitServiceImpl implements EnquiryCreditLimitService {
	@Override
	public Optional<AccountBalanceResponse> getCreditLimitAccount(String bankCode, String accountNo) {
		AccountBalanceResponse response = new AccountBalanceResponse();
		MasterBankAccount findAccNo = new MasterBankAccount();
		MasterMappingCode masterMappingCode = new MasterMappingCode();
		MasterMappingResponseException mappingResponseException = new MasterMappingResponseException();
		Map<String, MasterMappingCodeDetail> mappingCodeDetailDefaults = new HashMap<>();
		Map<String, MasterMappingCodeDetail> mappingCodeDetails = new HashMap<>();
		boolean isWriteLogTransaction = true;
		String account_no_encode = "";
		String bank_code_encode = "";
		try {
			// Step 1 : Check AccountNo Not Null
			Map<String, Object> resultValidate = isInvalidInputParameter(accountNo, bankCode);
			boolean isInValid = (Boolean) resultValidate.get(RESULT_KEY);
			if (isInValid) {
				isWriteLogTransaction = false;
				String resultParams = (String) resultValidate.get(RESULT_PARAMS);
				response.setEnquiryStatus(MappingStatus.FAILED);
				response.setFailureReasonCode(REASON_CODE_E01);
				response.setFailureReason(String.format(REASON_ACC_INVALID, resultParams));
				return Optional.of(response);
			}

			// Decode parameter
			Base62 base62 = Base62.createInstance();
			final byte[] de_account = base62.decode(accountNo.getBytes());
			account_no_encode = new String(de_account);
			final byte[] de_bank_code = base62.decode(bankCode.getBytes());
			bank_code_encode = new String(de_bank_code);

			// Step 2: Check MasterMapping Response Exception
			final String serviceType = ServiceType.ENQUIRY_CREDIT_LIMIT.getCode();
			mappingResponseException = mappingExceptionRepository.findMappingExceptionByBankCodeAccNoServiceType(
					bank_code_encode, account_no_encode, serviceType, EXCEPTION_STATUS);
			if (mappingResponseException != null) {
				isWriteLogTransaction = false;
				int delay = mappingResponseException.getDelay();
				DelayProcess.delay(delay);
				response.setAccountNo(account_no_encode);
				response.setEnquiryStatus(prepareMappingStatus(mappingResponseException.getTransactionStatus()));
				response.setFailureReason(mappingResponseException.getFailureReason());
				response.setFailureReasonCode(mappingResponseException.getFailureReasonCode());
				return Optional.of(response);
			}

			// Step 3 : Load Mapping Code Enquiry Account Balance By BankCode
			// and Mapping
			// Group
			masterMappingCode = masterMappingCodeRepository.findMasterMappingCodeByBankCode(bank_code_encode,
					serviceType);
			mappingCodeDetailDefaults = prepareMappingCodeDetail(null);
			mappingCodeDetails = prepareMappingCodeDetail(masterMappingCode);

			if (masterMappingCode == null) {
				masterMappingCode = new MasterMappingCode();
				masterMappingCode.setDelay(0);
			}
			DelayProcess.delay(masterMappingCode.getDelay());

			// Step3 : Check Bank Account
			findAccNo = accountRepository.findMasterBankAccountByAccountNoAndAccountType(account_no_encode);

			if (findAccNo == null || AccountType.CURRENT.getCode().equals(findAccNo.getAccountType())
					|| AccountType.SAVING.getCode().equals(findAccNo.getAccountType())) {
				MasterMappingCodeDetail mappingCodeDetailCaseAccountNotRegister = mappingCodeDetails
						.get(BankCaseEnums.ENQUIRY_CREDIT_LIMIT_FAILED_ACC_NOT_REGISTER.getCode());
				if (mappingCodeDetailCaseAccountNotRegister == null) {
					mappingCodeDetailCaseAccountNotRegister = mappingCodeDetailDefaults
							.get(BankCaseEnums.ENQUIRY_CREDIT_LIMIT_FAILED_ACC_NOT_REGISTER.getCode());
				}
				return Optional.of(prepareCreditLimitResponse(null, response, mappingCodeDetailCaseAccountNotRegister,
						account_no_encode));
			}
			MasterMappingCodeDetail mappingCodeDetailCaseSuccess = mappingCodeDetails
					.get(BankCaseEnums.ENQUIRY_CREDIT_LIMIT_SUCCESS.getCode());
			if (mappingCodeDetailCaseSuccess == null) {
				mappingCodeDetailCaseSuccess = mappingCodeDetailDefaults
						.get(BankCaseEnums.ENQUIRY_CREDIT_LIMIT_SUCCESS.getCode());
			}
			// Step 5 : Return Response
			return Optional.of(
					prepareCreditLimitResponse(findAccNo, response, mappingCodeDetailCaseSuccess, account_no_encode));

		} catch (Exception e) {
			response.setAccountNo(account_no_encode);
			response.setEnquiryStatus(MappingStatus.ERROR);
			response.setFailureReasonCode(REASON_CODE_E99);
			response.setFailureReason(REASON_EXCEPTION);
		} finally {
			// Step 6 : Log Transaction
			if (isWriteLogTransaction) {
				// Write Log Transaction
				MasterBankAccount findAccNoNew = checkAccount(findAccNo);
				LogTransaction log = new LogTransaction();
				log.setLogType(LogType.ENQUIRY_CREDIT_LIMIT.getCode());
				log.setBankCode(findAccNoNew.getBankCode());
				log.setSourceCustomerID(findAccNoNew.getCustomerID());
				log.setSourceAccountType(findAccNoNew.getAccountType());
				log.setSourceAccountNo(account_no_encode);
				log.setCurrencyCode(response.getCurrencyCode());
				log.setTransactionStatus(response.getEnquiryStatus().getCode());
				log.setFailureReason(response.getFailureReason());
				log.setFailureReasonCode(response.getFailureReasonCode());
				Date currentTime = new Date();
				log.setUpdateTime(currentTime);
				try {
					logTransactionRepository.save(log);
				} catch (Exception e2) {
				}
			}
		}
		return Optional.of(response);
	}

	private Map<String, Object> isInvalidInputParameter(String accountNo, String bankCode) {
		Map<String, Object> result = new HashMap<>();
		result.put(RESULT_KEY, false);
		result.put(RESULT_PARAMS, "");
		if (accountNo == null || "".equals(accountNo)) {
			result.put(RESULT_KEY, true);
			result.put(RESULT_PARAMS, "AccountNo is NULL");
			return result;
		}
		if (accountNo.length() > 50) {
			result.put(RESULT_KEY, true);
			result.put(RESULT_PARAMS, "AccountNo must no more than 50 digits.");
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
		return result;
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
					.findBankCaseByBankCaseGroup(ServiceType.ENQUIRY_CREDIT_LIMIT.getCode());
			if (!bankCase.isEmpty()) {
				for (MasterBankCase each : bankCase) {
					masterMappingCodeDetail.add(new MasterMappingCodeDetail(4L, each.getBankCaseCode(),
							each.getBankCaseStatus(), each.getDefaultReasonCode(), each.getBankCaseName()));
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return masterMappingCodeDetail;
	}

	private AccountBalanceResponse prepareCreditLimitResponse(MasterBankAccount findAccNo,
			AccountBalanceResponse response, MasterMappingCodeDetail mappingCodeDetail, String accountNo) {
		// Step5 : Set Response
		if (MappingStatus.SUCCESS.getCode().equals(mappingCodeDetail.getTransactionStatus())) {
			// Return Response
			response.setEnquiryStatus(MappingStatus.SUCCESS);
			response.setAccountNo(findAccNo.getAccountNo());
			response.setAccountType(prepareAccountType(findAccNo.getAccountType()));
			response.setCustomerId(findAccNo.getCustomerID());
			response.setCreditLimit(df.format(findAccNo.getCreditLimit()));
			response.setOutstanding(df.format(findAccNo.getOutstanding()));
			response.setAvailableBalance(df.format(findAccNo.getAvailable()));
			response.setCurrencyCode(CurrencyCode.THB.getCode());
			response.setAccountStatus(prepareAccountStatus(findAccNo.getAccountStatus()));
			response.setLatestTransactionTime(simpleDateTimeFormat.format(findAccNo.getUpdateTime()));
			response.setAccountBalance(df.format(findAccNo.getLedgerBalance()));
		} else {
			response.setAccountNo(accountNo);
			response.setEnquiryStatus(findMappingStatus(mappingCodeDetail.getTransactionStatus()));
			response.setFailureReasonCode(mappingCodeDetail.getFailureReasonCode());
			response.setFailureReason(mappingCodeDetail.getFailureReason());
		}
		return response;
	}

	private MappingStatus findMappingStatus(String status) {
		for (MappingStatus each : MappingStatus.values()) {
			if (each.getCode().equals(status)) {
				return each;
			}
		}
		return MappingStatus.ERROR;
	}

	private AccountType prepareAccountType(String accountType) {
		for (AccountType type : AccountType.values()) {
			if (accountType.equals(type.getCode())) {
				return type;
			}
		}
		return null;
	}

	private AccountStatus prepareAccountStatus(String accountStatus) {
		for (AccountStatus status : AccountStatus.values()) {
			if (accountStatus.equals(status.getCode())) {
				return status;
			}
		}
		return null;
	}

	private MappingStatus prepareMappingStatus(String transactionStatus) {
		for (MappingStatus status : MappingStatus.values()) {
			if (transactionStatus.equals(status.getCode())) {
				return status;
			}
		}
		return null;
	}

	private MasterBankAccount checkAccount(MasterBankAccount findAccNo) {
		if (findAccNo == null) {
			findAccNo = new MasterBankAccount();
		}
		return findAccNo;
	}

	private static final String REASON_CODE_E01 = "E01";
	private static final String REASON_CODE_E99 = "E99";
	private static final String RESULT_KEY = "RESULT";
	private static final String RESULT_PARAMS = "PARAMS";
	private static final String REASON_ACC_INVALID = "Invalid input parameter (%s).";
	private static final String EXCEPTION_STATUS = "AC";
	private static final String REASON_EXCEPTION = "Core Bank is Exception : Enquiry Credit Limit";
	private DecimalFormat df = new DecimalFormat("0.00");
	private SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("en"));

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private MasterMappingCodeRepository masterMappingCodeRepository;

	@Autowired
	private LogTransactionRepository logTransactionRepository;

	@Autowired
	private MappingExceptionRepository mappingExceptionRepository;

	@Autowired
	private BankCaseRepository bankCaseRepository;
}
