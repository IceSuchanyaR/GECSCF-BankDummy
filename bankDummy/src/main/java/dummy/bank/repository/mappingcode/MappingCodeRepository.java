package gec.scf.dummy.bank.repository.mappingcode;

import gec.scf.dummy.bank.entity.MasterMappingCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MappingCodeRepository extends JpaRepository<MasterMappingCode, Long>{
	
	public MasterMappingCode findMasterMappingCodeByServiceType(String mappingCode);
	
	@Query("select u from MasterMappingCode u where  u.serviceType like %?1% AND u.bankCode like %?2% ")
	public List<MasterMappingCode> findMasterMappingCode(String serviceType, String bankCode);

	public MasterMappingCode findMasterMappingCodeBymappingID(Long mappingID);

	@Query("select u from MasterMappingCode u where u.serviceType like %?1%  AND u.bankCode like %?2% order by u.updateTime ASC ")
	public List<MasterMappingCode> findByAndSortUpdateTimeAsc(String serviceType, String bankCode);

	@Query("select u from MasterMappingCode u where  u.serviceType like %?1% AND u.bankCode like %?2% ")
	public MasterMappingCode findMasterMappingCodeByServiceTypeBankCode(String serviceType, String bankCode);
	
	@Query("select u from MasterMappingCode u where u.bankCode = ?1 AND u.serviceType = ?2 ")
	public MasterMappingCode findMasterMappingCodeByBankCode(String bankCode, String serviceType);

	@Query("select u from MasterMappingCode u where u.bankCode = ?1 AND u.serviceType = ?2 ")
	public MasterMappingCode findMasterMappingCodeByAccNoAndServiceType(String bankCode, String serviceType);
}
