package gec.scf.dummy.bank.repository.bankcase;

import gec.scf.dummy.bank.entity.MasterBankCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankCaseRepository extends JpaRepository<MasterBankCase, String>{
	
	@Query("select u from MasterBankCase u where  u.bankCaseGroup = ?1 ")
	public List<MasterBankCase> findBybankCaseGroup(String bankCaseGroup);
	
	@Query("select u from MasterBankCase u where  u.bankCaseCode = ?1 ")
	public MasterBankCase findBybankCaseCode(String bankCaseCode);

	public List<MasterBankCase> findBankCaseByBankCaseGroup(String bankCaseGroup);
}
