package gec.scf.dummy.bank.repository.configapplication;

import gec.scf.dummy.bank.entity.ConfigApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigApplicationRepository extends JpaRepository<ConfigApplication, Long>{
	
	@Query("select u from ConfigApplication u where  u.configGroup like %?1% ")
	public List<ConfigApplication> findByconfigGroup(String configGroup);
	
	
	@Query("select u from ConfigApplication u where u.configGroup = ?1 ")
	public ConfigApplication findByconfigGroupBank(String configGroup);


	
}
