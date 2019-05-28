package gec.scf.dummy.bank.repository.mappingexception;

import gec.scf.dummy.bank.entity.MasterMappingResponseException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MappingExceptionRepository extends JpaRepository<MasterMappingResponseException, Long> {

   public MasterMappingResponseException findMasterMappingCodeByServiceType(String mappingException);
	
	@Query("select u from MasterMappingResponseException u where  u.serviceType like %?1% AND u.bankCode like %?2% AND u.accountNo like %?3% ")
	public List<MasterMappingResponseException> findMasterMappingException(String serviceType, String bankCode, String accountNo);

	public MasterMappingResponseException findByexceptionID(Long exceptionID);

	@Query("select u from MasterMappingResponseException u where u.serviceType like %?1%  AND u.bankCode like %?2% AND u.accountNo like %?3% order by u.updateTime ASC ")
	public List<MasterMappingResponseException> findByAndSortUpdateTimeAsc(String serviceType, String bankCode, String accountNo);
	
	@Query("select u from MasterMappingResponseException u where u.bankCode = ?1 AND u.accountNo = ?2 AND u.serviceType = ?3 AND u.status = ?4 ")
	public MasterMappingResponseException findMappingExceptionByBankCodeAccNoServiceType(String bankCode, String accountNo, String serviceType, String status);

	@Query("select u from MasterMappingResponseException u where  u.accountNo = ?1 AND u.serviceType = ?2 AND u.status = ?3  ")
	public MasterMappingResponseException findMappingExceptionByAccNoServiceType(String accountNo, String serviceType, String status);
}

