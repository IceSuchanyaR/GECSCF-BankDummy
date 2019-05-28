package gec.scf.dummy.bank.kbankws.account.service.impl;

import java.text.DecimalFormat;
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
import gec.scf.dummy.bank.kbankws.account.repository.AccountRepository;
import gec.scf.dummy.bank.kbankws.account.repository.MasterMappingCodeRepository;
import gec.scf.dummy.bank.kbankws.account.service.AccountService;
import gec.scf.dummy.bank.kbankws.core.config.DelayProcess;
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
public class AccountServiceImpl implements AccountService {

	private static final String REASON_CODE_E01 = "E01";
	private static final String REASON_CODE_E99 = "E99";
	private static final String RESULT_KEY = "RESULT";
	private static final String RESULT_PARAMS = "PARAMS";
	private static final String EXCEPTION_STATUS = "AC";
	private static final String REASON_ACC_INVALID = "Invalid input parameter (%s).";
	private static final String REASON_EXCEPTION = "Core Bank is Exception : Enquiry Account Balance";
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
	
	@Override
	public Optional<AccountBalanceResponse> getAccountBalanceByAccountNo(String bankCode, String accountNo) {
		AccountBalanceResponse response = new AccountBalanceResponse();
		MasterBankAccount findAccNo = new MasterBankAccount();
		MasterMappingCode masterMappingCode = new MasterMappingCode();
		MasterMappingResponseException mappingResponseException = new MasterMappingResponseException();
		Map<String, MasterMappingCodeDetail> mappingCodeDetailDefaults = new HashMap<>();
		Map<String, MasterMappingCodeDetail> mappingCodeDetails = new HashMap<>();
		boolean isWriteLogTransaction = true;
		String account_no_encode = new String();
		String bank_code_encode = new String();
		try {
			// step 1 : Check AccountNo Not Null
			Map<String, Object> resultValidate = isInvalidInputParameter(accountNo, bankCode);
			boolean isInValid = (Boolean) resultValidate.get(RESULT_KEY);
			if (isInValid) {
				isWriteLogTransaction = false;
				String resultParams = (String) resultValidate.get(RESULT_PARAMS);
				response.setEnquiryStatus(MappingStatus.FAILED);
				response.setFailureReasonCode(REASON_CODE_E01);
				response.setFailureReason(String.format(REASON_ACC_INVALID, resultParams));
				response.setLatestTransactionTime(simpleDateTimeFormat.format(new Date()));
				return Optional.of(response);
			}

			// Decode parameter
			Base62 base62 = Base62.createInstance();
			final byte[] de_account = base62.decode(accountNo.getBytes());
			account_no_encode = new String(de_account);
			final byte[] de_bank_code = base62.decode(bankCode.getBytes());
			bank_code_encode = new String(de_bank_code);

			// Step 2: Check MasterMapping Response Exception
			final String serviceType = ServiceType.ENQUIRY_ACCOUNT_BALANCE.getCode();
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
				response.setLatestTransactionTime(simpleDateTimeFormat.format(new Date()));
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

			// Step 4 : Check Bank Account
			findAccNo = accountRepository.findMasterBankAccountByAccountNoAndBankCode(account_no_encode,
					bank_code_encode);
			if (isValidFindAccountNo(findAccNo)) {
				MasterMappingCodeDetail mappingCodeDetailCaseAccountNotRegister = mappingCodeDetails
						.get(BankCaseEnums.ENQUIRY_ACCOUNT_FAILED_ACC_NOT_REGISTER.getCode());
				if (mappingCodeDetailCaseAccountNotRegister == null) {
					mappingCodeDetailCaseAccountNotRegister = mappingCodeDetailDefaults
							.get(BankCaseEnums.ENQUIRY_ACCOUNT_FAILED_ACC_NOT_REGISTER.getCode());
				}
				return Optional.of(prepareAccountBalanceResponse(null, response,
						mappingCodeDetailCaseAccountNotRegister, account_no_encode));
			}

			MasterMappingCodeDetail mappingCodeDetailCaseSuccess = mappingCodeDetails
					.get(BankCaseEnums.ENQUIRY_ACCOUNT_SUCCESS.getCode());
			if (mappingCodeDetailCaseSuccess == null) {
				mappingCodeDetailCaseSuccess = mappingCodeDetailDefaults
						.get(BankCaseEnums.ENQUIRY_ACCOUNT_SUCCESS.getCode());
			}
			// Step 5 : Return Response
			return Optional.of(prepareAccountBalanceResponse(findAccNo, response, mappingCodeDetailCaseSuccess,
					account_no_encode));
		} catch (Exception e) {
			response.setAccountNo(account_no_encode);
			response.setEnquiryStatus(MappingStatus.ERROR);
			response.setFailureReasonCode(REASON_CODE_E99);
			response.setFailureReason(REASON_EXCEPTION);
			response.setLatestTransactionTime(simpleDateTimeFormat.format(new Date()));
		} finally {
			if (isWriteLogTransaction) {
				// Step 6 : Log Transaction
				MasterBankAccount findaccNoNew = checkAccount(findAccNo);
				LogTransaction log = new LogTransaction();
				log.setLogType(LogType.ENQUIRY_ACCOUNT_BALANCE.getCode());
				log.setBankCode(findaccNoNew.getBankCode());
				log.setSourceCustomerID(findaccNoNew.getCustomerID());
				log.setSourceAccountType(findaccNoNew.getAccountType());
				log.setSourceAccountNo(findaccNoNew.getAccountNo());
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
					.findBankCaseByBankCaseGroup(ServiceType.ENQUIRY_ACCOUNT_BALANCE.getCode());
			if (!bankCase.isEmpty()) {
				for (MasterBankCase each : bankCase) {
					masterMappingCodeDetail.add(new MasterMappingCodeDetail(2L, each.getBankCaseCode(),
							each.getBankCaseStatus(), each.getDefaultReasonCode(), each.getBankCaseName()));
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return masterMappingCodeDetail;
	}

	private MappingStatus prepareMappingStatus(String transactionStatus) {
		for (MappingStatus status : MappingStatus.values()) {
			if (transactionStatus.equals(status.getCode())) {
				return status;
			}
		}
		return null;
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

	private boolean isValidFindAccountNo(MasterBankAccount findAccNo) {
		boolean result = false;
		try {
			if (findAccNo == null) {
				result = true;
			} else {
				if (AccountType.TERM_LOAN.getCode().equals(findAccNo.getAccountType())) {
					result = true;
				}
			}
		} catch (Exception e) {
		}
		return result;
	}

	private MasterBankAccount checkAccount(MasterBankAccount findaccNo) {
		if (findaccNo == null) {
			findaccNo = new MasterBankAccount();
		}
		return findaccNo;
	}

	private AccountBalanceResponse prepareAccountBalanceResponse(MasterBankAccount findAccNo,
			AccountBalanceResponse response, MasterMappingCodeDetail mappingCodeDetail, String accountNo) {
		if (MappingStatus.SUCCESS.getCode().equals(mappingCodeDetail.getTransactionStatus())) {
			response.setEnquiryStatus(MappingStatus.SUCCESS);
			response.setAccountNo(findAccNo.getAccountNo());
			response.setAccountType(prepareAccountType(findAccNo.getAccountType()));
			response.setCustomerId(findAccNo.getCustomerID());
			response.setAccountBalance(df.format(findAccNo.getLedgerBalance()));
			response.setLatestTransactionTime(simpleDateTimeFormat.format(findAccNo.getUpdateTime()));
			response.setAvailableBalance(df.format(findAccNo.getAvailable()));
			response.setCurrencyCode(CurrencyCode.THB.getCode());
			response.setAccountStatus(prepareAccountStatus(findAccNo.getAccountStatus()));
			if (AccountType.OVER_DRAFT.getCode().equals(findAccNo.getAccountType())) {
				response.setAccountBalance(df.format(findAccNo.getLedgerBalance()));
				response.setCreditLimit(df.format(findAccNo.getCreditLimit()));
				response.setOutstanding(df.format(findAccNo.getOutstanding()));
			}
		} else if (MappingStatus.FAILED.getCode().equals(mappingCodeDetail.getTransactionStatus())) {
			response.setAccountNo(accountNo);
			response.setEnquiryStatus(MappingStatus.FAILED);
			response.setFailureReasonCode(mappingCodeDetail.getFailureReasonCode());
			response.setFailureReason(mappingCodeDetail.getFailureReason());
			response.setLatestTransactionTime(simpleDateTimeFormat.format(new Date()));
		} else {
			response.setAccountNo(accountNo);
			response.setEnquiryStatus(MappingStatus.ERROR);
			response.setFailureReasonCode(mappingCodeDetail.getFailureReasonCode());
			response.setFailureReason(mappingCodeDetail.getFailureReason());
			response.setLatestTransactionTime(simpleDateTimeFormat.format(new Date()));
		}
		return response;
	}

	
}