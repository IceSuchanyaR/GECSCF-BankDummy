package gec.scf.dummy.bank.kbankws.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import gec.scf.dummy.bank.entity.MasterBankAccount;

@Repository
public interface AccountRepository extends JpaRepository<MasterBankAccount, Long> {

	public MasterBankAccount findMasterBankAccountByAccountNo(String accountNo);

	@Query("select u from MasterBankAccount u where  u.accountNo = ?1 AND u.bankCode = ?2 AND u.accountType = ?3 ")
	public MasterBankAccount findMasterBankAccountByAccountNoBankCodeAndAccountType(String accountNo, String bankCode,String accountType);

	@Query("select u from MasterBankAccount u where  u.accountNo = ?1 ")
	public MasterBankAccount findMasterBankAccountByAccountNoAndAccountType(String accountNo);

	@Query("select u from MasterBankAccount u where  u.accountNo = ?1 AND u.bankCode = ?2 ")
	public MasterBankAccount findMasterBankAccountByAccountNoAndBankCode(String accountNo, String bankCode);

	@Query("select u from MasterBankAccount u where  u.customerCode = ?1 AND u.customerCreditLimit = ?2 ")
	public MasterBankAccount findMasterBankAccountBySponsorCode(String customerCode, boolean customerCreditLimit);
}
