package gec.scf.dummy.bank.repository.mappingcodedetail;

import gec.scf.dummy.bank.entity.MasterMappingCodeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MappingCodeDetailRepository extends JpaRepository<MasterMappingCodeDetail, Long>{
	
	public List<MasterMappingCodeDetail> findByMappingID(Long mappingID);
}
