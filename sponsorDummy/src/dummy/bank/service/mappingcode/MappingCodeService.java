package gec.scf.dummy.bank.service.mappingcode;

import gec.scf.dummy.bank.entity.ConfigApplication;
import gec.scf.dummy.bank.entity.MasterBankCase;
import gec.scf.dummy.bank.entity.MasterMappingCode;

import java.util.List;


public interface MappingCodeService {

	public List<MasterMappingCode> getAllMappingCode(String serviceType, String bankCode, Long itemNo)throws Exception;

	public Long getTotalMappingCode(String serviceType, String bankCode)throws Exception;
	
	public List<ConfigApplication> getAllServiceType()throws Exception;
	
	public List<String> getBankCode()throws Exception;
	
	public List<MasterBankCase> getAllMasterBankCase(String serviceType)throws Exception;
	
	public MasterMappingCode newMappingCode(MasterMappingCode masterMappingCode)throws Exception;
	
	public MasterMappingCode editMappingCode(MasterMappingCode masterMappingCode)throws Exception;
	
	public MasterMappingCode deleteMappingCode(MasterMappingCode masterMappingCode)throws Exception;

	public MasterMappingCode findMasterMappingCodeBymappingID(Long mappingID)throws Exception;
	
	public Long getTotalMappingCodeDetail(Long mappingID);
	
	public MasterMappingCode checkDuplicate(String bankCode, String serviceType) throws Exception;

	public MasterBankCase getTransactionStatus(String bankCaseCode);
	

}
