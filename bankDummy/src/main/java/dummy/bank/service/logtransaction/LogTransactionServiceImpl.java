package gec.scf.dummy.bank.service.logtransaction;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gec.scf.dummy.bank.entity.ConfigApplication;
import gec.scf.dummy.bank.entity.LogTransaction;
import gec.scf.dummy.bank.eum.MappingStatus;
import gec.scf.dummy.bank.eum.MasterBankAccountType;
import gec.scf.dummy.bank.eum.ServiceType;
import gec.scf.dummy.bank.repository.configapplication.ConfigApplicationRepository;
import gec.scf.dummy.bank.repository.logtransaction.LogTransactionRepository;

@Service("LogTransactionServiceImpl")
public class LogTransactionServiceImpl implements Serializable, LogtransactionService {

	private static final long serialVersionUID = 1L;
	private static final String SERVICE_TYPE = "EXCEPTION_SERVICE_TYPE";

	@Autowired
	private LogTransactionRepository logTransactionRepository;

	@Autowired
	private ConfigApplicationRepository configApplicationRepository;

	@Override
	public List<LogTransaction> getAllLogtransaction(String logType, String bankTransactionNo, String sourceAccountNo,
													 String transactionStatus, Long itemNo) throws Exception {
		List<LogTransaction> results = null;
		try {
			if (isBlank(logType) && isBlank(bankTransactionNo) && isBlank(sourceAccountNo)
					&& isBlank(transactionStatus)) {
				// Find All
				results = logTransactionRepository.findLogAll();
			} else if (isNotBlank(logType) && isBlank(bankTransactionNo) && isBlank(sourceAccountNo)
					&& isBlank(transactionStatus)) {
				// Find by Log Type
				results = logTransactionRepository.findLogTransactionByLogTypeOrderByUpdateTimeDESC(logType);
			} else if (isBlank(logType) && isNotBlank(bankTransactionNo) && isBlank(sourceAccountNo)
					&& isBlank(transactionStatus)) {
				// Find by BankTransactionNo
				results = logTransactionRepository
						.findLogTransactionByBankTransactionNoOrderByUpdateTimeDESC(bankTransactionNo);
			} else if (isBlank(logType) && isBlank(bankTransactionNo) && isNotBlank(sourceAccountNo)
					&& isBlank(transactionStatus)) {
				// Find by Source Account No
				results = logTransactionRepository
						.findLogTransactionBySourceAccountNoOrderByUpdateTimeDESC(sourceAccountNo);
			} else if (isBlank(logType) && isBlank(bankTransactionNo) && isBlank(sourceAccountNo)
					&& isNotBlank(transactionStatus)) {
				// Find by Transaction Status
				results = logTransactionRepository
						.findLogTransactionByTransactionStatusOrderByUpdateTimeDESC(transactionStatus);
			} else if (isNotBlank(logType) && isNotBlank(bankTransactionNo) && isBlank(sourceAccountNo)
					&& isBlank(transactionStatus)) {
				// Find by Log Type and Bank Transaction
				results = logTransactionRepository.findLogTransactionByLogTypeAndBankTransactionNoOrderByUpdateTimeDESC(
						logType, bankTransactionNo);
			} else if (isNotBlank(logType) && isBlank(bankTransactionNo) && isNotBlank(sourceAccountNo)
					&& isBlank(transactionStatus)) {
				// Find by Log Type and Source Account No
				results = logTransactionRepository
						.findLogTransactionByLogTypeAndSourceAccountNoOrderByUpdateTimeDESC(logType, sourceAccountNo);
			} else if (isNotBlank(logType) && isBlank(bankTransactionNo) && isBlank(sourceAccountNo)
					&& isNotBlank(transactionStatus)) {
				// Find by Log Type and Transaction Status
				results = logTransactionRepository.findLogTransactionByLogTypeAndTransactionStatusOrderByUpdateTimeDESC(
						logType, transactionStatus);
			} else if (isBlank(logType) && isNotBlank(bankTransactionNo) && isNotBlank(sourceAccountNo)
					&& isBlank(transactionStatus)) {
				// Find by Bank Transaction No and Source Account No
				results = logTransactionRepository
						.findLogTransactionBySourceAccountNoAndBankTransactionNoOrderByUpdateTimeDESC(sourceAccountNo,
								bankTransactionNo);
			} else if (isBlank(logType) && isNotBlank(bankTransactionNo) && isBlank(sourceAccountNo)
					&& isNotBlank(transactionStatus)) {
				// Find by Bank Transaction No and Transaction Status
				results = logTransactionRepository
						.findLogTransactionByTransactionStatusAndBankTransactionNoOrderByUpdateTimeDESC(
								transactionStatus, bankTransactionNo);
			} else if (isBlank(logType) && isBlank(bankTransactionNo) && isNotBlank(sourceAccountNo)
					&& isNotBlank(transactionStatus)) {
				// Find by Source Account No and Transaction Status
				results = logTransactionRepository
						.findLogTransactionBySourceAccountNoAndTransactionStatusOrderByUpdateTimeDESC(sourceAccountNo,
								transactionStatus);
			} else if (isNotBlank(logType) && isNotBlank(bankTransactionNo) && isNotBlank(sourceAccountNo)
					&& isBlank(transactionStatus)) {
				// Find by Log Type and Bank Transaction No and Source Account
				// No
				results = logTransactionRepository
						.findLogTransactionByLogTypeAndSourceAccountNoAndBankTransactionNoOrderByUpdateTimeDESC(logType,
								sourceAccountNo, bankTransactionNo);
			} else if (isBlank(logType) && isNotBlank(bankTransactionNo) && isNotBlank(sourceAccountNo)
					&& isNotBlank(transactionStatus)) {
				// Find by Bank Transaction No and Source Account No
				results = logTransactionRepository
						.findLogTransactionBySourceAccountNoAndTransactionStatusAndBankTransactionNoOrderByUpdateTimeDESC(
								sourceAccountNo, transactionStatus, bankTransactionNo);
			} else if (isNotBlank(logType) && isNotBlank(bankTransactionNo) && isBlank(sourceAccountNo)
					&& isNotBlank(transactionStatus)) {
				// Find by Log Type and Bank Transaction No and Transaction
				// Status
				results = logTransactionRepository
						.findLogTransactionByLogTypeAndBankTransactionNoAndTransactionStatusOrderByUpdateTimeDESC(
								logType, bankTransactionNo, transactionStatus);
			} else {
				// Find not null
				results = logTransactionRepository.findLogTransactionAllUpdateTimeDESC(logType, sourceAccountNo,
						transactionStatus, bankTransactionNo);
			}
			if (results.isEmpty()) {
				results = new ArrayList<>();
			}
		} catch (Exception e) {

		}
		return setPropertiesLogTransaction(results, itemNo);
	}

	@Override
	public Long getTotalLogtransaction(String logType, String bankTransactionNo, String sourceAccountNo,
									   String transactionStatus, Long itemNo) throws Exception {
		Long result = 0L;
		List<LogTransaction> results = getAllLogtransaction(logType, bankTransactionNo, sourceAccountNo,
				transactionStatus, itemNo);
		if (results.isEmpty()) {
			return result;
		}
		return Long.valueOf(results.size());
	}

	public LogTransaction findLogTransactionBylogID(Long logID) throws Exception {
		LogTransaction findLogTransaction = null;
		Map<String, ConfigApplication> configGroupItems = loadConfigGroup();
		try {
			if (logID == null) {
				throw new Exception("MappingID is Null");
			}

			findLogTransaction = logTransactionRepository.findLogTransactionBylogID(logID);

		} catch (Exception e) {
			throw e;
		}

		return prepareLogTransactionDisplay(configGroupItems, findLogTransaction);
	}

	@Override
	public List<ConfigApplication> getAllLogType() {
		List<ConfigApplication> results = null;

		try {
			results = configApplicationRepository.findByconfigGroup(SERVICE_TYPE);

			if (results.isEmpty()) {
				results = new ArrayList<>();
			}

		} catch (Exception e) {
		}
		return results;
	}

	private List<LogTransaction> setPropertiesLogTransaction(List<LogTransaction> logTransactions, Long itemNo) {
		List<LogTransaction> results = new ArrayList<>();
		LogTransaction logTransaction = null;

		int item = 1;
		if (itemNo == null) {
			itemNo = Long.valueOf(logTransactions.size());
		}

		Map<String, ConfigApplication> configGroupItems = loadConfigGroup();

		for (LogTransaction log : logTransactions) {
			logTransaction = prepareLogTransactionDisplay(configGroupItems, log);
			logTransaction.setItemNo(Long.valueOf(item));
			item++;
			results.add(logTransaction);
			itemNo--;
			if (itemNo == 0) {
				break;
			}
		}
		return results;
	}

	private Map<String, ConfigApplication> loadConfigGroup() {
		// load Config Group
		Map<String, ConfigApplication> configGroupItems = new HashMap<>();
		List<ConfigApplication> loadConfigGroupList = configApplicationRepository.findByconfigGroup(SERVICE_TYPE);
		if (!loadConfigGroupList.isEmpty()) {
			for (ConfigApplication each : loadConfigGroupList) {
				configGroupItems.put(each.getConfigValue(), each);
			}
		}
		return configGroupItems;
	}

	private LogTransaction prepareLogTransactionDisplay(Map<String, ConfigApplication> configGroups,
														LogTransaction logtTransaction) {
		logtTransaction.setItemNo(Long.valueOf(1));
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("en"));
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd", new Locale("en"));

		if (configGroups.isEmpty()) {
			if (ServiceType.ENQUIRY_ACCOUNT_BALANCE.getCode().equals(logtTransaction.getLogType())) {
				logtTransaction.setLogTypeDisplay(ServiceType.ENQUIRY_ACCOUNT_BALANCE.getDescription());
			}
			if (ServiceType.ENQUIRY_CREDIT_LIMIT.getCode().equals(logtTransaction.getLogType())) {
				logtTransaction.setLogTypeDisplay(ServiceType.ENQUIRY_CREDIT_LIMIT.getDescription());
			}
			if (ServiceType.DIRECTDEBIT.getCode().equals(logtTransaction.getLogType())) {
				logtTransaction.setLogTypeDisplay(ServiceType.DIRECTDEBIT.getDescription());
			}
			if (ServiceType.DRAWDOWN.getCode().equals(logtTransaction.getLogType())) {
				logtTransaction.setLogTypeDisplay(ServiceType.DRAWDOWN.getDescription());
			}
			if (ServiceType.ENQUIRY_DRAWDOWN.getCode().equals(logtTransaction.getLogType())) {
				logtTransaction.setLogTypeDisplay(ServiceType.ENQUIRY_DRAWDOWN.getDescription());
			}
			if (ServiceType.ENQUIRY_DIRECT_DEBIT.getCode().equals(logtTransaction.getLogType())) {
				logtTransaction.setLogTypeDisplay(ServiceType.ENQUIRY_DIRECT_DEBIT.getDescription());
			}
		} else {
			ConfigApplication configGroup = configGroups.get(logtTransaction.getLogType());
			if (configGroup == null) {
				logtTransaction.setLogTypeDisplay(" ");
			} else {
				logtTransaction.setLogTypeDisplay(configGroup.getConfigDisplay());
			}
		}
		if (logtTransaction.getTransactionDate() != null) {
			logtTransaction.setTransactionDateDisplay(formatDate.format(logtTransaction.getTransactionDate()));
		}
		if (logtTransaction.getFailureReasonCodeView() == null) {
			logtTransaction.setFailureReasonCodeView("-");
		}
		if (logtTransaction.getFailureReasonView() == null) {
			logtTransaction.setFailureReasonView("-");
		}
		if (logtTransaction.getBankTransactionNoView() == null) {
			logtTransaction.setBankTransactionNoView("-");
		}
		if (MasterBankAccountType.BANK_ACCOUNT_TYPE_CURRENT.getCode()
				.equals(logtTransaction.getSponsorCreditLimitAccountType())) {
			logtTransaction.setSponsorCreditLimitAccountTypeDisplay(
					MasterBankAccountType.BANK_ACCOUNT_TYPE_CURRENT.getDescription());
		}
		if (MasterBankAccountType.BANK_ACCOUNT_TYPE_SAVING.getCode()
				.equals(logtTransaction.getSponsorCreditLimitAccountType())) {
			logtTransaction.setSponsorCreditLimitAccountTypeDisplay(
					MasterBankAccountType.BANK_ACCOUNT_TYPE_SAVING.getDescription());
		}
		if (MasterBankAccountType.BANK_ACCOUNT_TYPE_OVERDRAFT.getCode()
				.equals(logtTransaction.getSponsorCreditLimitAccountType())) {
			logtTransaction.setSponsorCreditLimitAccountTypeDisplay(
					MasterBankAccountType.BANK_ACCOUNT_TYPE_OVERDRAFT.getDescription());
		}
		if (MasterBankAccountType.BANK_ACCOUNT_TYPE_DRAWDOWN.getCode()
				.equals(logtTransaction.getSponsorCreditLimitAccountType())) {
			logtTransaction.setSponsorCreditLimitAccountTypeDisplay(
					MasterBankAccountType.BANK_ACCOUNT_TYPE_DRAWDOWN.getDescription());
		}

		if (MasterBankAccountType.BANK_ACCOUNT_TYPE_CURRENT.getCode().equals(logtTransaction.getSourceAccountType())) {
			logtTransaction
					.setSourceAccountTypeDisplay(MasterBankAccountType.BANK_ACCOUNT_TYPE_CURRENT.getDescription());
		}
		if (MasterBankAccountType.BANK_ACCOUNT_TYPE_SAVING.getCode().equals(logtTransaction.getSourceAccountType())) {
			logtTransaction
					.setSourceAccountTypeDisplay(MasterBankAccountType.BANK_ACCOUNT_TYPE_SAVING.getDescription());
		}
		if (MasterBankAccountType.BANK_ACCOUNT_TYPE_OVERDRAFT.getCode()
				.equals(logtTransaction.getSourceAccountType())) {
			logtTransaction
					.setSourceAccountTypeDisplay(MasterBankAccountType.BANK_ACCOUNT_TYPE_OVERDRAFT.getDescription());
		}
		if (MasterBankAccountType.BANK_ACCOUNT_TYPE_DRAWDOWN.getCode().equals(logtTransaction.getSourceAccountType())) {
			logtTransaction
					.setSourceAccountTypeDisplay(MasterBankAccountType.BANK_ACCOUNT_TYPE_DRAWDOWN.getDescription());
		}

		if (MasterBankAccountType.BANK_ACCOUNT_TYPE_CURRENT.getCode()
				.equals(logtTransaction.getDestinationAccountType())) {
			logtTransaction
					.setDestinationAccountTypeDisplay(MasterBankAccountType.BANK_ACCOUNT_TYPE_CURRENT.getDescription());
		}
		if (MasterBankAccountType.BANK_ACCOUNT_TYPE_SAVING.getCode()
				.equals(logtTransaction.getDestinationAccountType())) {
			logtTransaction
					.setDestinationAccountTypeDisplay(MasterBankAccountType.BANK_ACCOUNT_TYPE_SAVING.getDescription());
		}
		if (MasterBankAccountType.BANK_ACCOUNT_TYPE_OVERDRAFT.getCode()
				.equals(logtTransaction.getDestinationAccountType())) {
			logtTransaction.setDestinationAccountTypeDisplay(
					MasterBankAccountType.BANK_ACCOUNT_TYPE_OVERDRAFT.getDescription());
		}
		if (MasterBankAccountType.BANK_ACCOUNT_TYPE_DRAWDOWN.getCode()
				.equals(logtTransaction.getDestinationAccountType())) {
			logtTransaction.setDestinationAccountTypeDisplay(
					MasterBankAccountType.BANK_ACCOUNT_TYPE_DRAWDOWN.getDescription());
		}

		if (MappingStatus.SUCCESS.getCode().equals(logtTransaction.getTransactionStatus())) {
			logtTransaction.setTransactionStatusDisplay(MappingStatus.SUCCESS.getDescription());
		}

		if (MappingStatus.FAILED.getCode().equals(logtTransaction.getTransactionStatus())) {
			logtTransaction.setTransactionStatusDisplay(MappingStatus.FAILED.getDescription());
		}
		if (MappingStatus.ERROR.getCode().equals(logtTransaction.getTransactionStatus())) {
			logtTransaction.setTransactionStatusDisplay(MappingStatus.ERROR.getDescription());
		}

		return logtTransaction;
	}

	private boolean isBlank(String value) {
		boolean result = false;
		try {
			if (value == null || "".equals(value)) {
				result = true;
			}
		} catch (Exception e) {
		}
		return result;
	}

	private boolean isNotBlank(String value) {
		return !isBlank(value);
	}
}
