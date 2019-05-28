package gec.scf.dummy.bank.repository.account;

import gec.scf.dummy.bank.entity.MasterBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisterAccountRepository extends JpaRepository<MasterBankAccount, Long> {

	public MasterBankAccount findMasterBankAccountByAccountID(Long accountID);

	public List<MasterBankAccount> findByaccountNoStartingWith(String accountNo);

	public Long countByaccountNo(String accountNo);

	@Query("select u from MasterBankAccount u where u.accountNo like %?1% order by u.accountNo ASC ")
	public List<MasterBankAccount> findByAndSortAccountNoAsc(String accountNo);

	@Query("select u from MasterBankAccount u where  u.accountNo = ?1 ")
	public List<MasterBankAccount> findBybankCodeAndaccountNoAndaccountType(String accountNo);

	@Query("select distinct c.bankCode from MasterBankAccount c order by c.bankCode ASC ")
	public List<String> findBankCode();

	@Query("select u from MasterBankAccount u where  u.customerCode = ?1 AND u.customerCreditLimit = ?2 and u.accountNo != ?3 ")
    public MasterBankAccount findMasterBankAccountBySponsorCode(String customerCode, boolean customerCreditLimit, String accountNo);
	
	public MasterBankAccount findMasterBankAccountByAccountNo(String accountNo);

	@Query("select u from MasterBankAccount u where  u.accountNo = ?1 AND u.bankCode = ?2 AND u.accountType = ?3 ")
	public MasterBankAccount findMasterBankAccountByAccountNoBankCodeAndAccountType(String accountNo, String bankCode, String accountType);

	@Query("select u from MasterBankAccount u where  u.accountNo = ?1 ")
	public MasterBankAccount findMasterBankAccountByAccountNoAndAccountType(String accountNo);

	@Query("select u from MasterBankAccount u where  u.accountNo = ?1 AND u.bankCode = ?2 ")
	public MasterBankAccount findMasterBankAccountByAccountNoAndBankCode(String accountNo, String bankCode);

}
