package gec.scf.dummy.bank.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import gec.scf.dummy.bank.entity.BankManageEvent;
import gec.scf.dummy.bank.entity.ConfigApplication;
import gec.scf.dummy.bank.entity.MasterBankAccount;
import gec.scf.dummy.bank.eum.MasterBankAccountType;
import gec.scf.dummy.bank.service.account.RegisterAccountService;

@RestController
@PreAuthorize("hasAnyAuthority('MANAGE_BANK','MANAGE_ALL')")
@RequestMapping(value = "/registerBankAccount")
public class RegisterAccountController implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(RegisterAccountController.class);
	
	// URL : /registerBankAccount/

	@Autowired
	private RegisterAccountService registerAccountService;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@RequestMapping(value = "/bankAccountList", method = RequestMethod.GET)
	public List<MasterBankAccount> getAllBankAccount(
			@RequestParam(value = "accountNo", required = false) String accountNo,
			@RequestParam(value = "itemNo", required = false) Long itemNo) {
		List<MasterBankAccount> results = null;
		try {
			results = registerAccountService.getAllBankAccount(accountNo, itemNo);
		} catch (Exception e) {
			logger.error("getAllBankAccount error!!!", e);
			results = new ArrayList<>();
		}
		return results;
	}

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@RequestMapping(value = "/getAccountType", method = RequestMethod.GET)
	public List<ConfigApplication> getAllAccountType() throws Exception {
		List<ConfigApplication> results = null;

		try {
			results = registerAccountService.getAllAccountType();

		} catch (Exception e) {
			logger.error("getAllAccountType error!!!", e);
			results = new ArrayList<>();
		}
		return results;
	}
	
	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@RequestMapping(value = "/getCurrencyCode", method = RequestMethod.GET)
	public List<ConfigApplication> getAllCurrencyCode() throws Exception {
		List<ConfigApplication> results = null;

		try {
			results = registerAccountService.getAllCurrencyCode();

		} catch (Exception e) {
			logger.error("getAllCurrencyCode error!!!", e);
			results = new ArrayList<>();
		}
		return results;
	}

	@RequestMapping(value = "/totalBankAccount", method = RequestMethod.GET)
	public Long getTotalBankAccount(@RequestParam(value = "accountNo", required = false) String accountNo)
			throws Exception {
		return registerAccountService.getTotalBankAccount(accountNo);
	}

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@RequestMapping(value = "/manageBankAccount", method = RequestMethod.POST)
	public MasterBankAccount manageBankAccount(@RequestBody MasterBankAccount masterBankAccount) {
		MasterBankAccount masterBankAccountResponse = new MasterBankAccount();
		try {
			// valid
			if (isValidBankAccount(masterBankAccount)) {
				if (masterBankAccount.isEditMode()) {
					masterBankAccountResponse = registerAccountService.editBankAccount(masterBankAccount);
				} else {
					masterBankAccountResponse = registerAccountService.newBankAccount(masterBankAccount);
				}
				if (masterBankAccountResponse == null) {
					masterBankAccountResponse = new MasterBankAccount();
				}
			}
		} catch (Exception e) {
			logger.error("manageBankAccount error!!!", e);
			masterBankAccountResponse.setErrorMessage("Submit Bank Account Fail  :  " + e.getMessage());
			masterBankAccountResponse.setErrorMessageStatus("FAIL");
		}
		return masterBankAccountResponse;
	}

	private boolean isValidBankAccount(MasterBankAccount masterBankAccount) throws Exception {
		boolean result = true;
		if (masterBankAccount == null) {
			throw new Exception("MasterBankAccount is Null.");
		}
		if (masterBankAccount.getBankCode() == null || "".equals(masterBankAccount.getBankCode())) {
			throw new Exception("BankCode is Null.");
		}
		if (masterBankAccount.getBankCode().length() > 50) {
			throw new Exception("BankCode must no more than 50 characters.");
		}
		if (masterBankAccount.getAccountNo() == null || "".equals(masterBankAccount.getAccountNo())) {
			throw new Exception("AccountNo is Null.");
		} else {
			if (masterBankAccount.getAccountNo().length() > 20) {
				throw new Exception("AccountNo must no more than 20 characters.");
			}
			try {
				new BigDecimal(masterBankAccount.getAccountNo());
			} catch (Exception e) {
				throw new Exception("AccountNo is Invalid.");
			}
		}

		if (masterBankAccount.getAccountType() == null || "".equals(masterBankAccount.getAccountType())) {
			throw new Exception("AccountType is Null.");
		} else {
			if (MasterBankAccountType.BANK_ACCOUNT_TYPE_CURRENT.getCode().equals(masterBankAccount.getAccountType())
					|| MasterBankAccountType.BANK_ACCOUNT_TYPE_SAVING.getCode()
							.equals(masterBankAccount.getAccountType())) {
				if (masterBankAccount.getLedgerBalance() == null) {
					throw new Exception("Ledger balance is Invalid.");
				}
				if (String.valueOf(masterBankAccount.getLedgerBalance()).length() > 15) {
					throw new Exception("Ledger balance must no more than 15 digits.");
				}
			} else if (MasterBankAccountType.BANK_ACCOUNT_TYPE_DRAWDOWN.getCode()
					.equals(masterBankAccount.getAccountType())) {
				if (masterBankAccount.getCreditLimit() == null) {
					throw new Exception("CreditLimit is Invalid.");
				}
				if (String.valueOf(masterBankAccount.getCreditLimit()).length() > 15) {
					throw new Exception("CreditLimit must no more than 15 digits.");
				}
			} else {
				if (masterBankAccount.getLedgerBalance() == null) {
					throw new Exception("Ledger balance is Invalid.");
				}
				if (String.valueOf(masterBankAccount.getLedgerBalance()).length() > 18) {
					throw new Exception("LedgerBalance must no more than 18 digits.");
				}
				if (masterBankAccount.getCreditLimit() == null) {
					throw new Exception("CreditLimit is Invalid.");
				}
				if (String.valueOf(masterBankAccount.getCreditLimit()).length() > 18) {
					throw new Exception("CreditLimit must no more than 18 digits.");
				}
			}
		}

		if (masterBankAccount.getAccountStatus() == null || "".equals(masterBankAccount.getAccountStatus())) {
			throw new Exception("AccountStatus is Null.");
		}
		return result;
	}

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@RequestMapping(value = "/deleteBankAccount", method = RequestMethod.DELETE)
	public MasterBankAccount deleteBankAccount(@RequestParam(value = "accountID", required = false) Long accountID) {
		MasterBankAccount findBankAccount = null;
		try {
			findBankAccount = registerAccountService.findBankAccountByIDForDelete(accountID);
			if (findBankAccount == null) {
				throw new Exception("Bank Account is Not Found.");
			}
			registerAccountService.deleteBankAccount(findBankAccount);
		} catch (Exception e) {
			logger.error("deleteBankAccount error!!!", e);
			findBankAccount = new MasterBankAccount();
			findBankAccount.setErrorMessage("Delete Bank Account Fail  :  " + e.getMessage());
			findBankAccount.setErrorMessageStatus("FAIL");
		}
		return findBankAccount;
	}

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@RequestMapping(value = "/bankAccountView", method = RequestMethod.GET)
	public MasterBankAccount getBankAccountByAccountID(
			@RequestParam(value = "accountID", required = false) Long accountID) {
		MasterBankAccount result = null;
		try {
			result = registerAccountService.findBankAccountByID(accountID);
		} catch (Exception e) {
			logger.error("getBankAccountByAccountID error!!!", e);
			result = new MasterBankAccount();
		}
		return result;
	}
}
