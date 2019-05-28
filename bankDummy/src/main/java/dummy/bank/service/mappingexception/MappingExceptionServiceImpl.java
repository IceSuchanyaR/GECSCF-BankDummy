package gec.scf.dummy.bank.service.mappingexception;

import gec.scf.dummy.bank.entity.ConfigApplication;
import gec.scf.dummy.bank.entity.MasterMappingResponseException;
import gec.scf.dummy.bank.eum.ExceptionStatus;
import gec.scf.dummy.bank.eum.MappingStatus;
import gec.scf.dummy.bank.eum.ServiceType;
import gec.scf.dummy.bank.repository.configapplication.ConfigApplicationRepository;
import gec.scf.dummy.bank.repository.mappingexception.MappingExceptionRepository;
import gec.scf.dummy.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("MappingExceptionServiceImpl")
public class MappingExceptionServiceImpl implements Serializable, MappingExceptionService {

	private static final long serialVersionUID = 1L;
	private static final String SERVICE_TYPE = "EXCEPTION_SERVICE_TYPE";


	@Autowired
	private MappingExceptionRepository mappingExceptionRepository;


	@Autowired
	private ConfigApplicationRepository configApplicationRepository;



	@Override
	public List<MasterMappingResponseException> getAllMappingException(String serviceType, String bankCode, String accountNo, Long itemNo)
			throws Exception {
		List<MasterMappingResponseException> results = null;
			if (serviceType == null) {
				serviceType = "";
			}
			if (bankCode == null) {
				bankCode = "";
			}
			if (accountNo == null) {
				accountNo = "";
			}

			results = mappingExceptionRepository.findByAndSortUpdateTimeAsc(serviceType, bankCode,accountNo);
			if (results.isEmpty()) {
				results = new ArrayList<>();
			}
		return setPropertiesMappingException(results, itemNo);
	}

	@Override
	public Long getTotalMappingException(String serviceType, String bankCode,String accountNo) {
		Long result = 0L;
		if (serviceType == null) {
			serviceType = "";
		}
		if (bankCode == null) {
			bankCode = "";
		}
		if (accountNo == null) {
			accountNo = "";
		}
		List<MasterMappingResponseException> results = mappingExceptionRepository.findMasterMappingException(serviceType, bankCode,accountNo);
		if (results.isEmpty()) {
			return result;
		}
		return Long.valueOf(results.size());
	}


	@Override
	public List<ConfigApplication> getAllServiceType() throws Exception {
		List<ConfigApplication> results = null;

		try {
			results = configApplicationRepository.findByconfigGroup(SERVICE_TYPE);

			if (results.isEmpty()) {
				results = new ArrayList<>();
			}

		} catch (Exception e) {
		}
		return results;
	}



	@Override
	public MasterMappingResponseException newMappingException(MasterMappingResponseException masterMappingException) throws Exception {
		List<MasterMappingResponseException> findMappingCode = null;
		try {
			if (masterMappingException == null) {
				throw new Exception("MasterMappingException is Null");
			}
			// Validate MappingCode
			findMappingCode = mappingExceptionRepository.findMasterMappingException(masterMappingException.getServiceType(),
					masterMappingException.getBankCode(),masterMappingException.getAccountNo());
			if (!findMappingCode.isEmpty()) {
				throw new Exception("MappingException is Duplicated.");
			}
			Date currentTime = new Date();
			masterMappingException.setCreateBy(SecurityUtils.getUser().getId());
			masterMappingException.setCreateTime(currentTime);
			masterMappingException.setUpdateBy(SecurityUtils.getUser().getId());
			masterMappingException.setUpdateTime(currentTime);
			MasterMappingResponseException mappingException = mappingExceptionRepository.save(masterMappingException);
			if (mappingException != null) {
				return mappingException;
			}
		} catch (Exception e) {
			throw e;
		}

		return null;
	}

	@Override
	public MasterMappingResponseException editMappingException(MasterMappingResponseException masterMappingException)throws Exception {
		MasterMappingResponseException findMappingException = null;
		try {
			if (masterMappingException == null) {
				throw new Exception("Mapping Response Exception is Null");
			}
			Integer version = masterMappingException.getVersion();
			findMappingException = mappingExceptionRepository.findByexceptionID(masterMappingException.getExceptionID());

			if (findMappingException == null) {
				throw new Exception("masterMappingException is not found.");
			}
			if (findMappingException.getVersion() != version) {
				throw new OptimisticLockException("masterMappingException data in db will change");
			}

			findMappingException.setDelay(masterMappingException.getDelay());
			findMappingException.setTransactionStatus(masterMappingException.getTransactionStatus());
			findMappingException.setStatus(masterMappingException.getStatus());
			findMappingException.setFailureReason(masterMappingException.getFailureReason());
			findMappingException.setFailureReasonCode(masterMappingException.getFailureReasonCode());
			findMappingException.setUpdateTime(new Date());
			findMappingException.setUpdateBy(SecurityUtils.getUser().getId());
			mappingExceptionRepository.save(findMappingException);
		} catch (Exception e) {
			throw e;
		}
		return findMappingException;



	}

	@Override
	public MasterMappingResponseException deleteMappingException(MasterMappingResponseException masterMappingException) throws Exception {
		MasterMappingResponseException findMappingException = null;
		try {

			if (masterMappingException == null) {
				throw new Exception("masterMappingException is Null");
			}
			Integer version = masterMappingException.getVersion();
			findMappingException = mappingExceptionRepository.findByexceptionID(masterMappingException.getExceptionID());
			if (findMappingException == null) {
				throw new Exception("Mapping Response Exception is not found.");
			}

			if (findMappingException.getVersion() != version) {
				throw new OptimisticLockException("masterMappingException data in db will change");
			}

			mappingExceptionRepository.delete(findMappingException);
		} catch (Exception e) {
			throw e;
		}
		return findMappingException;
	}

	private List<MasterMappingResponseException> setPropertiesMappingException(List<MasterMappingResponseException> mappingExceptions, Long itemNo) {
		List<MasterMappingResponseException> results = new ArrayList<>();
		MasterMappingResponseException mappingException = null;

		int item = 1;
		if (itemNo == null) {
			itemNo = Long.valueOf(mappingExceptions.size());
		}

		Map<String, ConfigApplication> configGroupItems = loadConfigGroup();

		for (MasterMappingResponseException mappingexception : mappingExceptions) {
			mappingException = prepareMappingExceptionDisplay(configGroupItems, mappingexception);
			mappingException.setItemNo(Long.valueOf(item));
			item++;
			results.add(mappingException);
			itemNo--;
			if (itemNo == 0) {
				break;
			}
		}
		return results;
	}

	private Map<String, ConfigApplication> loadConfigGroup() {
		// load Config Group
		Map<String, ConfigApplication> configGroupItems = new HashMap<>();
		List<ConfigApplication> loadConfigGroupList = configApplicationRepository.findByconfigGroup(SERVICE_TYPE);
		if (!loadConfigGroupList.isEmpty()) {
			for (ConfigApplication each : loadConfigGroupList) {
				configGroupItems.put(each.getConfigValue(), each);
			}
		}
		return configGroupItems;
	}

	private MasterMappingResponseException prepareMappingExceptionDisplay(Map<String, ConfigApplication> configGroups,
			MasterMappingResponseException mappingException) {
		mappingException.setItemNo(Long.valueOf(1));
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("en"));

		if (configGroups.isEmpty()) {
			if (ServiceType.ENQUIRY_ACCOUNT_BALANCE.getCode().equals(mappingException.getServiceType())) {
				mappingException.setServiceTypeDisplay(ServiceType.ENQUIRY_ACCOUNT_BALANCE.getDescription());
			}
			if (ServiceType.ENQUIRY_CREDIT_LIMIT.getCode().equals(mappingException.getServiceType())) {
				mappingException.setServiceTypeDisplay(ServiceType.ENQUIRY_CREDIT_LIMIT.getDescription());
			}
			if (ServiceType.DIRECTDEBIT.getCode().equals(mappingException.getServiceType())) {
				mappingException.setServiceTypeDisplay(ServiceType.DIRECTDEBIT.getDescription());
			}
			if (ServiceType.DRAWDOWN.getCode().equals(mappingException.getServiceType())) {
				mappingException.setServiceTypeDisplay(ServiceType.DRAWDOWN.getDescription());
			}
			if (ServiceType.ENQUIRY_DIRECT_DEBIT.getCode().equals(mappingException.getServiceType())) {
				mappingException.setServiceTypeDisplay(ServiceType.ENQUIRY_DIRECT_DEBIT.getDescription());
			}
			if (ServiceType.ENQUIRY_DRAWDOWN.getCode().equals(mappingException.getServiceType())) {
				mappingException.setServiceTypeDisplay(ServiceType.ENQUIRY_DRAWDOWN.getDescription());
			}
		} else {
			ConfigApplication configGroup = configGroups.get(mappingException.getServiceType());
			if (configGroup == null) {
				mappingException.setServiceTypeDisplay("-");
			} else {
				mappingException.setServiceTypeDisplay(configGroup.getConfigDisplay());
			}
		}


		if(MappingStatus.SUCCESS.getCode().equals(mappingException.getTransactionStatus())) {
			mappingException.setTransactionStatusDisplay(MappingStatus.SUCCESS.getDescription());
		}

		if(MappingStatus.FAILED.getCode().equals(mappingException.getTransactionStatus())) {
			mappingException.setTransactionStatusDisplay(MappingStatus.FAILED.getDescription());
		}
		if(MappingStatus.ERROR.getCode().equals(mappingException.getTransactionStatus())) {
			mappingException.setTransactionStatusDisplay(MappingStatus.ERROR.getDescription());
		}

		if(ExceptionStatus.AC.getCode().equals(mappingException.getStatus())) {
			mappingException.setStatusDisplay(ExceptionStatus.AC.getDescription());
		}
		if(ExceptionStatus.IN.getCode().equals(mappingException.getStatus())) {
			mappingException.setStatusDisplay(ExceptionStatus.IN.getDescription());
		}


		if (mappingException.getUpdateTime() != null) {
			mappingException.setUpdateTimeDisplay(format.format(mappingException.getUpdateTime()));
		}
		if (mappingException.getCreateTime() != null) {
			mappingException.setCreateTimeDisplay(format.format(mappingException.getCreateTime()));
		}


		return mappingException;
	}




	public MasterMappingResponseException findMasterMappingExceptionByexceptionID(Long exceptionID) throws Exception {
		MasterMappingResponseException findMappingException = null;
		Map<String, ConfigApplication> configGroupItems = loadConfigGroup();
		try {
			if (exceptionID == null) {
				throw new Exception("ExceptionID is Null");
			}

			findMappingException = mappingExceptionRepository.findByexceptionID(exceptionID);

		} catch (Exception e) {
			throw e;
		}

		return prepareMappingExceptionDisplay(configGroupItems, findMappingException);
	}






}
