package gec.scf.dummy.bank.service.account;

import gec.scf.dummy.bank.entity.ConfigApplication;
import gec.scf.dummy.bank.entity.MasterBankAccount;

import java.util.List;

public interface RegisterAccountService {

	public List<MasterBankAccount> getAllBankAccount(String accountNo, Long itemNo) throws Exception;

	public Long getTotalBankAccount(String accountNo) throws Exception;

	public MasterBankAccount newBankAccount(MasterBankAccount masterBankAccount) throws Exception;

	public MasterBankAccount editBankAccount(MasterBankAccount masterBankAccount) throws Exception;

	public MasterBankAccount deleteBankAccount(MasterBankAccount masterBankAccount) throws Exception;

	public MasterBankAccount findBankAccountByID(Long accountID) throws Exception;
	
	public MasterBankAccount findBankAccountByIDForDelete(Long accountID) throws Exception;

	public List<ConfigApplication> getAllAccountType() throws Exception;

	public List<ConfigApplication> getAllCurrencyCode() throws Exception;
}
