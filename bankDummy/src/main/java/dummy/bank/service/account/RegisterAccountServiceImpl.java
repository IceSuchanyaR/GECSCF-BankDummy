package gec.scf.dummy.bank.service.account;

import gec.scf.dummy.bank.entity.ConfigApplication;
import gec.scf.dummy.bank.entity.MasterBankAccount;
import gec.scf.dummy.bank.eum.MasterBankAccountStatus;
import gec.scf.dummy.bank.eum.MasterBankAccountType;
import gec.scf.dummy.bank.repository.account.RegisterAccountRepository;
import gec.scf.dummy.bank.repository.configapplication.ConfigApplicationRepository;
import gec.scf.dummy.common.utils.SecurityUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service("RegisterAccountServiceImpl")
public class RegisterAccountServiceImpl implements Serializable, RegisterAccountService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String ACCOUNT_TYPE = "ACCOUNT_TYPE";
	private static final String CURRENCY_CODE = "CURRENCY_CODE";

	private static final Logger logger = LoggerFactory.getLogger(RegisterAccountServiceImpl.class);

	@Autowired
	private RegisterAccountRepository registerAccountRepository;

	@Autowired
	private ConfigApplicationRepository configApplicationRepository;

	@Override
	public List<MasterBankAccount> getAllBankAccount(String accountNo, Long itemNo) throws Exception {
		List<MasterBankAccount> results = null;
		try {
			if (accountNo == null) {
				accountNo = "";
			}

			results = registerAccountRepository.findByAndSortAccountNoAsc(accountNo);
			if (results.isEmpty()) {
				results = new ArrayList<>();
			}
		} catch (Exception e) {
			logger.error("getAllBankAccount error!!!", e);
		}
		return setPropertiesBankAccount(results, itemNo);
	}

	private List<MasterBankAccount> setPropertiesBankAccount(List<MasterBankAccount> bankAccounts, Long itemNo) {
		List<MasterBankAccount> results = new ArrayList<>();
		MasterBankAccount bankAccount = null;

		int item = 1;
		if (itemNo == null) {
			itemNo = Long.valueOf(bankAccounts.size());
		}
		for (MasterBankAccount acc : bankAccounts) {
			bankAccount = prepareBankAccountDisplay(acc);
			bankAccount.setItemNo(Long.valueOf(item));
			item++;
			results.add(bankAccount);
			itemNo--;
			if (itemNo == 0) {
				break;
			}
		}
		return results;
	}

	private MasterBankAccount prepareBankAccountDisplay(MasterBankAccount bankAccount) {
		bankAccount.setItemNo(Long.valueOf(1));
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("en"));
		if (MasterBankAccountStatus.BANK_ACCOUNT_STATUS_ACTIVE.getCode().equals(bankAccount.getAccountStatus())) {
			bankAccount.setAccountStatusDisplay(MasterBankAccountStatus.BANK_ACCOUNT_STATUS_ACTIVE.getDescription());
		} else {
			bankAccount.setAccountStatusDisplay(MasterBankAccountStatus.BANK_ACCOUNT_STATUS_SUSPEND.getDescription());
		}
		if (MasterBankAccountType.BANK_ACCOUNT_TYPE_CURRENT.getCode().equals(bankAccount.getAccountType())) {
			bankAccount.setAccountTypeDisplay(MasterBankAccountType.BANK_ACCOUNT_TYPE_CURRENT.getDescription());
		} else if (MasterBankAccountType.BANK_ACCOUNT_TYPE_SAVING.getCode().equals(bankAccount.getAccountType())) {
			bankAccount.setAccountTypeDisplay(MasterBankAccountType.BANK_ACCOUNT_TYPE_SAVING.getDescription());
		} else if (MasterBankAccountType.BANK_ACCOUNT_TYPE_OVERDRAFT.getCode().equals(bankAccount.getAccountType())) {
			bankAccount.setAccountTypeDisplay(MasterBankAccountType.BANK_ACCOUNT_TYPE_OVERDRAFT.getDescription());
		} else {
			bankAccount.setAccountTypeDisplay(MasterBankAccountType.BANK_ACCOUNT_TYPE_DRAWDOWN.getDescription());
		}
		if (bankAccount.getUpdateTime() != null) {
			bankAccount.setUpdateTimeDisplay(format.format(bankAccount.getUpdateTime()));
		}
		if (bankAccount.getCreateTime() != null) {
			bankAccount.setCreateTimeDisplay(format.format(bankAccount.getCreateTime()));
		}
		return bankAccount;
	}

	@Override
	public Long getTotalBankAccount(String accountNo) throws Exception {
		Long result = 0L;
		if (accountNo == null) {
			accountNo = "";
		}
		List<MasterBankAccount> results = registerAccountRepository.findByAndSortAccountNoAsc(accountNo);
		if (results.isEmpty()) {
			return result;
		}
		return Long.valueOf(results.size());
	}

	@Override
	public MasterBankAccount newBankAccount(MasterBankAccount masterBankAccount) throws Exception {
		try {
			if (masterBankAccount == null) {
				throw new Exception("masterBankAccount is Null");
			}

			// Validate Account
			List<MasterBankAccount> findAccount = registerAccountRepository
					.findBybankCodeAndaccountNoAndaccountType(masterBankAccount.getAccountNo());
			if (!findAccount.isEmpty()) {
				throw new Exception("Bank Account is Duplicated.");
			}
			if (MasterBankAccountType.BANK_ACCOUNT_TYPE_CURRENT.getCode().equals(masterBankAccount.getAccountType())
					|| MasterBankAccountType.BANK_ACCOUNT_TYPE_SAVING.getCode()
							.equals(masterBankAccount.getAccountType())) {
				masterBankAccount.setCreditLimit(new BigDecimal("0.00"));
				masterBankAccount.setOutstanding(new BigDecimal("0.00"));
				BigDecimal availableAmount = masterBankAccount.getLedgerBalance();
				masterBankAccount.setAvailable(availableAmount);
			} else if (MasterBankAccountType.BANK_ACCOUNT_TYPE_DRAWDOWN.getCode()
					.equals(masterBankAccount.getAccountType())) {
				if (masterBankAccount.isCustomerCreditLimit()) {
					if (isVerifyDuplicateCustomerCredit(masterBankAccount.getCustomerCode(),
							masterBankAccount.isCustomerCreditLimit(), masterBankAccount.getAccountNo())) {
						throw new Exception("This Customer Code already has a Credit Limit.");
					}
				}
				masterBankAccount.setLedgerBalance(new BigDecimal("0.00"));
				masterBankAccount.setOutstanding(new BigDecimal("0.00"));
				BigDecimal availableAmount = (masterBankAccount.getCreditLimit()
						.subtract(masterBankAccount.getOutstanding()));
				masterBankAccount.setAvailable(availableAmount);
			} else {
				if (masterBankAccount.isCustomerCreditLimit()) {
					if (isVerifyDuplicateCustomerCredit(masterBankAccount.getCustomerCode(),
							masterBankAccount.isCustomerCreditLimit(), masterBankAccount.getAccountNo())) {
						throw new Exception("This Customer Code already has a Credit Limit.");
					}
				}
				if (masterBankAccount.getLedgerBalance().compareTo(new BigDecimal("0.00")) > 0) {
					masterBankAccount.setOutstanding(new BigDecimal("0.00"));
				} else {
					masterBankAccount.setOutstanding(masterBankAccount.getLedgerBalance().abs());
				}
				BigDecimal availableAmount = (masterBankAccount.getCreditLimit()
						.add(masterBankAccount.getLedgerBalance()));
				masterBankAccount.setAvailable(availableAmount);
			}
			Date currentTime = new Date();
			masterBankAccount.setCurrencyCode(masterBankAccount.getCurrencyCode());
			masterBankAccount.setCreateBy(SecurityUtils.getUser().getId());
			masterBankAccount.setCreateTime(currentTime);
			masterBankAccount.setUpdateBy(SecurityUtils.getUser().getId());
			masterBankAccount.setUpdateTime(currentTime);
			MasterBankAccount acc = registerAccountRepository.save(masterBankAccount);
			if (acc != null) {
				return acc;
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	private boolean isVerifyDuplicateCustomerCredit(String customerCode, boolean customerCreditLimit,
			String accountNo) {
		boolean result = false;
		try {
			MasterBankAccount findSponsorAccount = registerAccountRepository
					.findMasterBankAccountBySponsorCode(customerCode, customerCreditLimit, accountNo);
			if (findSponsorAccount != null) {
				result = true;
			}
			if (findSponsorAccount == null) {
				result = false;
			}
		} catch (Exception e) {
			logger.error("isVerifyDuplicateCustomerCredit error!!!", e);
		}
		return result;
	}

	@Override
	public MasterBankAccount editBankAccount(MasterBankAccount masterBankAccount) throws Exception {
		MasterBankAccount findAcc = null;
		try {

			if (masterBankAccount == null) {
				throw new Exception("masterBankAccount is Null");
			}
			Integer version = masterBankAccount.getVersion();
			findAcc = registerAccountRepository.findMasterBankAccountByAccountID(masterBankAccount.getAccountID());
			if (findAcc == null) {
				throw new Exception("masterBankAccount is not found.");
			}
			if (findAcc.getVersion() != version) {
				throw new OptimisticLockException("masterBankAccount data in db will change");
			}

			if (MasterBankAccountType.BANK_ACCOUNT_TYPE_CURRENT.getCode().equals(findAcc.getAccountType())
					|| MasterBankAccountType.BANK_ACCOUNT_TYPE_SAVING.getCode().equals(findAcc.getAccountType())) {
				findAcc.setLedgerBalance(masterBankAccount.getLedgerBalance());
			} else if (MasterBankAccountType.BANK_ACCOUNT_TYPE_DRAWDOWN.getCode()
					.equals(masterBankAccount.getAccountType())) {
				if (masterBankAccount.isCustomerCreditLimit()) {
					if (isVerifyDuplicateCustomerCredit(masterBankAccount.getCustomerCode(),
							masterBankAccount.isCustomerCreditLimit(), masterBankAccount.getAccountNo())) {
						throw new Exception("This Customer Code already has a Credit Limit.");
					}
				}
				findAcc.setCreditLimit(masterBankAccount.getCreditLimit());
				findAcc.setOutstanding(masterBankAccount.getOutstanding());
			} else {
				if (masterBankAccount.isCustomerCreditLimit()) {
					if (isVerifyDuplicateCustomerCredit(masterBankAccount.getCustomerCode(),
							masterBankAccount.isCustomerCreditLimit(), masterBankAccount.getAccountNo())) {
						throw new Exception("This Customer Code already has a Credit Limit.");
					}
				}
				findAcc.setCreditLimit(masterBankAccount.getCreditLimit());
				findAcc.setLedgerBalance(masterBankAccount.getLedgerBalance());
				findAcc.setOutstanding(masterBankAccount.getOutstanding());
			}

			BigDecimal availableAmount = (masterBankAccount.getCreditLimit().add(masterBankAccount.getLedgerBalance())
					.subtract(masterBankAccount.getOutstanding()));
			findAcc.setAvailable(availableAmount);
			findAcc.setAccountStatus(masterBankAccount.getAccountStatus());
			findAcc.setUpdateBy(SecurityUtils.getUser().getId());
			findAcc.setUpdateTime(new Date());
			findAcc.setCustomerCreditLimit(masterBankAccount.isCustomerCreditLimit());
			registerAccountRepository.save(findAcc);
		} catch (Exception e) {
			throw e;
		}
		return findAcc;
	}

	@Override
	public MasterBankAccount deleteBankAccount(MasterBankAccount masterBankAccount) throws Exception {
		MasterBankAccount findAcc = null;
		try {
			if (masterBankAccount == null) {
				throw new Exception("masterBankAccount is Null");
			}
			Integer version = masterBankAccount.getVersion();
			findAcc = registerAccountRepository.findMasterBankAccountByAccountID(masterBankAccount.getAccountID());
			if (findAcc == null) {
				throw new Exception("Bank Account is not found.");
			}

			if (findAcc.getVersion() != version) {
				throw new OptimisticLockException("masterBankAccount data in db will change");
			}
			registerAccountRepository.delete(findAcc);
		} catch (Exception e) {
			throw e;
		}
		return findAcc;
	}

	@Override
	public MasterBankAccount findBankAccountByID(Long accountID) throws Exception {
		MasterBankAccount bankAccount = null;
		try {
			if (accountID == null) {
				throw new Exception("AccountID is Null");
			}
			bankAccount = registerAccountRepository.findMasterBankAccountByAccountID(accountID);
		} catch (Exception e) {
			throw e;
		}
		return prepareBankAccountDisplay(bankAccount);
	}

	@Override
	public MasterBankAccount findBankAccountByIDForDelete(Long accountID) throws Exception {
		MasterBankAccount bankAccount = null;
		try {
			if (accountID == null) {
				throw new Exception("AccountID is Null");
			}
			bankAccount = registerAccountRepository.findMasterBankAccountByAccountID(accountID);
		} catch (Exception e) {
			throw e;
		}
		return bankAccount;
	}

	@Override
	public List<ConfigApplication> getAllAccountType() throws Exception {
		List<ConfigApplication> results = null;

		try {
			results = configApplicationRepository.findByconfigGroup(ACCOUNT_TYPE);

			if (results.isEmpty()) {
				results = new ArrayList<>();
			}
		} catch (Exception e) {
			logger.error("getAllAccountType error!!!", e);
		}
		return results;
	}

	@Override
	public List<ConfigApplication> getAllCurrencyCode() throws Exception {
		List<ConfigApplication> results = null;
		try {
			results = configApplicationRepository.findByconfigGroup(CURRENCY_CODE);

			if (results.isEmpty()) {
				results = new ArrayList<>();
			}
		} catch (Exception e) {
			logger.error("getAllAccountType error!!!", e);
		}
		return results;
	}
}
