package dummy.bank.kbankws.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dummy.bank.entity.MasterMappingCode;

@Repository
public interface MasterMappingCodeRepository extends JpaRepository<MasterMappingCode, Long> {
 
	
	@Query("select u from MasterMappingCode u where u.bankCode = ?1 AND u.serviceType = ?2 ")
	public MasterMappingCode findMasterMappingCodeByBankCode(String bankCode,String serviceType);
	
	@Query("select u from MasterMappingCode u where u.bankCode = ?1 AND u.serviceType = ?2 ")
	public MasterMappingCode findMasterMappingCodeByAccNoAndServiceType(String bankCode,String serviceType);
	
}
