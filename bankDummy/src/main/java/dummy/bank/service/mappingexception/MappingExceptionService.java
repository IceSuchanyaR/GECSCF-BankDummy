package gec.scf.dummy.bank.service.mappingexception;

import gec.scf.dummy.bank.entity.ConfigApplication;
import gec.scf.dummy.bank.entity.MasterMappingResponseException;

import java.util.List;

public interface MappingExceptionService {

	public List<MasterMappingResponseException> getAllMappingException(String serviceType, String bankCode, String accountNo, Long itemNo)throws Exception;

	public Long getTotalMappingException(String serviceType, String bankCode, String accountNo)throws Exception;
	
	public List<ConfigApplication> getAllServiceType()throws Exception;
	
	public MasterMappingResponseException newMappingException(MasterMappingResponseException masterMappingException)throws Exception;
	
	public MasterMappingResponseException editMappingException(MasterMappingResponseException masterMappingException)throws Exception;
	
	public MasterMappingResponseException deleteMappingException(MasterMappingResponseException masterMappingException)throws Exception;

	public MasterMappingResponseException findMasterMappingExceptionByexceptionID(Long exceptionID)throws Exception;
	
	

	
}
