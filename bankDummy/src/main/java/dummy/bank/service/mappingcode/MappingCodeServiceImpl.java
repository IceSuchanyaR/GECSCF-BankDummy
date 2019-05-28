package gec.scf.dummy.bank.service.mappingcode;

import gec.scf.dummy.bank.entity.ConfigApplication;
import gec.scf.dummy.bank.entity.MasterBankCase;
import gec.scf.dummy.bank.entity.MasterMappingCode;
import gec.scf.dummy.bank.entity.MasterMappingCodeDetail;
import gec.scf.dummy.bank.eum.MappingStatus;
import gec.scf.dummy.bank.eum.ServiceType;
import gec.scf.dummy.bank.repository.account.RegisterAccountRepository;
import gec.scf.dummy.bank.repository.bankcase.BankCaseRepository;
import gec.scf.dummy.bank.repository.configapplication.ConfigApplicationRepository;
import gec.scf.dummy.bank.repository.mappingcode.MappingCodeRepository;
import gec.scf.dummy.bank.repository.mappingcodedetail.MappingCodeDetailRepository;
import gec.scf.dummy.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("MappingCodeServiceImpl")
public class MappingCodeServiceImpl implements Serializable, MappingCodeService {

	private static final long serialVersionUID = 1L;
	private static final String SERVICE_TYPE = "MAPPINGCODE_SERVICE_TYPE";

	@Autowired
	private MappingCodeRepository mappingCodeRepository;

	@Autowired
	private MappingCodeDetailRepository mappingCodeDetailRepository;

	@Autowired
	private ConfigApplicationRepository configApplicationRepository;

	@Autowired
	private RegisterAccountRepository registerAccountRepository;

	@Autowired
	private BankCaseRepository bankCaseRepository;

	@Override
	public List<MasterMappingCode> getAllMappingCode(String serviceType, String bankCode, Long itemNo)
			throws Exception {
		List<MasterMappingCode> results = null;
		try {
			if (serviceType == null) {
				serviceType = "";
			}
			if (bankCode == null) {
				bankCode = "";
			}

			results = mappingCodeRepository.findByAndSortUpdateTimeAsc(serviceType, bankCode);
			if (results.isEmpty()) {
				results = new ArrayList<>();
			}
		} catch (Exception e) {

		}
		return setPropertiesMappingCode(results, itemNo);
	}

	@Override
	public Long getTotalMappingCode(String serviceType, String bankCode) {
		Long result = 0L;
		if (serviceType == null) {
			serviceType = "";
		}
		if (bankCode == null) {
			bankCode = "";
		}
		List<MasterMappingCode> results = mappingCodeRepository.findMasterMappingCode(serviceType, bankCode);
		if (results.isEmpty()) {
			return result;
		}
		return Long.valueOf(results.size());
	}

	@Override
	public Long getTotalMappingCodeDetail(Long mappingID) {
		Long result = 0L;

		List<MasterMappingCodeDetail> results = mappingCodeDetailRepository.findByMappingID(mappingID);

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
	public List<String> getBankCode() {
		List<String> results = null;
		try {
			results = registerAccountRepository.findBankCode();
		} catch (Exception e) {
		}
		return results;
	}

	@Override
	public List<MasterBankCase> getAllMasterBankCase(String serviceType) {
		List<MasterBankCase> results = null;
		if (serviceType == null) {
			serviceType = "";
		}
		results = bankCaseRepository.findBybankCaseGroup(serviceType);
		if (results.isEmpty()) {
			return results;
		}
		return results;
	}

	@Override
	public MasterMappingCode newMappingCode(MasterMappingCode masterMappingCode) throws Exception {
		List<MasterMappingCode> findMappingCode = null;
		MasterMappingCode mappingCode = null;
		try {
			if (masterMappingCode == null) {
				throw new Exception("MasterMappingCode is Null");
			}
			if (masterMappingCode.getMasterMappingCodeDetail().isEmpty()) {
				throw new Exception("MasterMappingCodeDetail is Null");
			}
			List<MasterMappingCodeDetail> mappingCodeDetails = new ArrayList<>();
			mappingCodeDetails.addAll(masterMappingCode.getMasterMappingCodeDetail());

			// Validate MappingCode
			findMappingCode = mappingCodeRepository.findMasterMappingCode(masterMappingCode.getServiceType(),
					masterMappingCode.getBankCode());
			if (!findMappingCode.isEmpty()) {
				throw new Exception("MappingCode is Duplicated.");
			}
			Date currentTime = new Date();
			masterMappingCode.setCreateBy(SecurityUtils.getUser().getId());
			masterMappingCode.setCreateTime(currentTime);
			masterMappingCode.setUpdateBy(SecurityUtils.getUser().getId());
			masterMappingCode.setUpdateTime(currentTime);

			masterMappingCode.setMasterMappingCodeDetail(null);
			mappingCode = mappingCodeRepository.save(masterMappingCode);
			if (mappingCode == null) {
				throw new Exception("Save Mapping Response Bank Fail");
			}
			for (MasterMappingCodeDetail each : mappingCodeDetails) {
				each.setMappingID(mappingCode.getMappingID());
				each.setMasterMappingCode(masterMappingCode);
				each.setUpdateTime(currentTime);
			}
			List<MasterMappingCodeDetail> saveDetails = mappingCodeDetailRepository.saveAll(mappingCodeDetails);
			if (saveDetails.isEmpty()) {
				throw new Exception("Save Mapping Response Bank Detail Fail");
			}
			return mappingCode;
		} catch (Exception e) {
			if(mappingCode != null) {
				mappingCodeRepository.delete(mappingCode);
				e = new Exception("Save Mapping Response Bank Fail ");
			}
			throw e;
		}
	}

	@Override
	public MasterMappingCode editMappingCode(MasterMappingCode masterMappingCode) throws Exception {
		MasterMappingCode findMappingCode = null;
		try {
			if (masterMappingCode == null) {
				throw new Exception("MappingCode is Null");
			}
			List<MasterMappingCodeDetail> mappingCodeDetails = new ArrayList<>();
			mappingCodeDetails.addAll(masterMappingCode.getMasterMappingCodeDetail());

			Integer version = masterMappingCode.getVersion();
			findMappingCode = mappingCodeRepository.findMasterMappingCodeBymappingID(masterMappingCode.getMappingID());

			if (findMappingCode == null) {
				throw new Exception("masterMappingCode is not found.");
			}
			if (findMappingCode.getVersion() != version) {
				throw new OptimisticLockException("masterMappingCode data in db will change");
			}

			List<MasterMappingCodeDetail> mappingCodeDetailList = new ArrayList<>();
			mappingCodeDetailList.addAll(findMappingCode.getMasterMappingCodeDetail());

			findMappingCode.setDelay(masterMappingCode.getDelay());
			masterMappingCode.setMasterMappingCodeDetail(null);
			MasterMappingCode mappingCode = mappingCodeRepository.save(masterMappingCode);
			if (mappingCode == null) {
				throw new Exception("Save Mapping Response Bank Fail");
			}
			// Delete
			for (MasterMappingCodeDetail each : mappingCodeDetailList) {
				mappingCodeDetailRepository.delete(each);
			}

			Date currentDateTime = new Date();
			for (MasterMappingCodeDetail each : mappingCodeDetails) {
				each.setMappingID(mappingCode.getMappingID());
				each.setMasterMappingCode(masterMappingCode);
				each.setUpdateTime(currentDateTime);
			}

			List<MasterMappingCodeDetail> saveDetails = mappingCodeDetailRepository.saveAll(mappingCodeDetails);
			if (saveDetails.isEmpty()) {
				throw new Exception("Save Mapping Response Bank Detail Fail");
			}

			return mappingCode;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public MasterMappingCode deleteMappingCode(MasterMappingCode masterMappingCode) throws Exception {
		MasterMappingCode findMappingCode = null;
		try {

			if (masterMappingCode == null) {
				throw new Exception("masterMappingCode is Null");
			}
			Integer version = masterMappingCode.getVersion();
			findMappingCode = mappingCodeRepository.findMasterMappingCodeBymappingID(masterMappingCode.getMappingID());
			if (findMappingCode == null) {
				throw new Exception("Mapping Code is not found.");
			}

			if (findMappingCode.getVersion() != version) {
				throw new OptimisticLockException("masterMappingCode data in db will change");
			}

			mappingCodeRepository.delete(findMappingCode);
		} catch (Exception e) {
			throw e;
		}
		return findMappingCode;
	}

	private List<MasterMappingCode> setPropertiesMappingCode(List<MasterMappingCode> mappingCodes, Long itemNo) {
		List<MasterMappingCode> results = new ArrayList<>();
		MasterMappingCode mappingCode = null;

		int item = 1;
		if (itemNo == null) {
			itemNo = Long.valueOf(mappingCodes.size());
		}

		Map<String, ConfigApplication> configGroupItems = loadConfigGroup();

		for (MasterMappingCode mappingcode : mappingCodes) {
			mappingCode = prepareMappingCodeDisplay(configGroupItems, mappingcode);
			mappingCode.setItemNo(Long.valueOf(item));
			item++;
			results.add(mappingCode);
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

	private MasterMappingCode prepareMappingCodeDisplay(Map<String, ConfigApplication> configGroups,
			MasterMappingCode mappingcode) {
		mappingcode.setItemNo(Long.valueOf(1));
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("en"));

		if (configGroups.isEmpty()) {
			if (ServiceType.ENQUIRY_ACCOUNT_BALANCE.getCode().equals(mappingcode.getServiceType())) {
				mappingcode.setServiceTypeDisplay(ServiceType.ENQUIRY_ACCOUNT_BALANCE.getDescription());
			}
			if (ServiceType.ENQUIRY_CREDIT_LIMIT.getCode().equals(mappingcode.getServiceType())) {
				mappingcode.setServiceTypeDisplay(ServiceType.ENQUIRY_CREDIT_LIMIT.getDescription());
			}
			if (ServiceType.DIRECTDEBIT.getCode().equals(mappingcode.getServiceType())) {
				mappingcode.setServiceTypeDisplay(ServiceType.DIRECTDEBIT.getDescription());
			}
			if (ServiceType.DRAWDOWN.getCode().equals(mappingcode.getServiceType())) {
				mappingcode.setServiceTypeDisplay(ServiceType.DRAWDOWN.getDescription());
			}
		} else {
			ConfigApplication configGroup = configGroups.get(mappingcode.getServiceType());
			if (configGroup == null) {
				mappingcode.setServiceTypeDisplay("-");
			} else {
				mappingcode.setServiceTypeDisplay(configGroup.getConfigDisplay());
			}
		}

		if (mappingcode.getUpdateTime() != null) {
			mappingcode.setUpdateTimeDisplay(format.format(mappingcode.getUpdateTime()));
		}
		if (mappingcode.getCreateTime() != null) {
			mappingcode.setCreateTimeDisplay(format.format(mappingcode.getCreateTime()));
		}
		return mappingcode;
	}

	public MasterMappingCode findMasterMappingCodeBymappingID(Long mappingID) throws Exception {
		MasterMappingCode findMappingCode = null;
		try {
			if (mappingID == null) {
				throw new Exception("MappingID is Null");
			}

			findMappingCode = mappingCodeRepository.findMasterMappingCodeBymappingID(mappingID);

		} catch (Exception e) {
			throw e;
		}

		return setPropertiesMappingCodeDetail(findMappingCode);
	}

	private MasterMappingCode setPropertiesMappingCodeDetail(MasterMappingCode mappingCode) {
		MasterMappingCodeDetail mappingCodeDetailResult = null;
		List<MasterMappingCodeDetail> mappingCodeDetailList = new ArrayList<>();
		MasterMappingCode result = new MasterMappingCode();
		Map<String, ConfigApplication> configGroupItems = loadConfigGroup();

		int item = 1;

		List<MasterMappingCodeDetail> mappingCodeDetails = mappingCode.getMasterMappingCodeDetail();
		Long itemNo = Long.valueOf(mappingCodeDetails.size());
		Map<String, MasterBankCase> bankCaseItems = loadBankCase(mappingCode.getServiceType());

		for (MasterMappingCodeDetail mappingCodeDetail : mappingCodeDetails) {
			mappingCodeDetailResult = prepareMappingCodeDetailDisplay(bankCaseItems, mappingCodeDetail);
			mappingCodeDetailResult.setItemNo(Long.valueOf(item));
			item++;
			mappingCodeDetailList.add(mappingCodeDetailResult);
			itemNo--;
			if (itemNo == 0) {
				break;
			}
		}
		result = prepareMappingCodeDisplay(configGroupItems, mappingCode);
		result.setMasterMappingCodeDetail(mappingCodeDetailList);

		return result;
	}

	private Map<String, MasterBankCase> loadBankCase(String serviceType) {
		// load Bank Case
		Map<String, MasterBankCase> bankCaseItems = new HashMap<>();
		List<MasterBankCase> loadBankCaseList = bankCaseRepository.findBybankCaseGroup(serviceType);
		if (!loadBankCaseList.isEmpty()) {
			for (MasterBankCase each : loadBankCaseList) {
				bankCaseItems.put(each.getBankCaseCode(), each);
			}
		}

		return bankCaseItems;
	}

	private MasterMappingCodeDetail prepareMappingCodeDetailDisplay(Map<String, MasterBankCase> bankCaseItems,
			MasterMappingCodeDetail mappingCodeDetail) {
		mappingCodeDetail.setItemNo(Long.valueOf(1));
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("en"));
		MasterBankCase bankCase = bankCaseItems.get(mappingCodeDetail.getBankCaseCode());
		if (MappingStatus.SUCCESS.getCode().equals(mappingCodeDetail.getTransactionStatus())) {
			mappingCodeDetail.setTransactionStatusDisplay(MappingStatus.SUCCESS.getDescription());
		}
		if (MappingStatus.FAILED.getCode().equals(mappingCodeDetail.getTransactionStatus())) {
			mappingCodeDetail.setTransactionStatusDisplay(MappingStatus.FAILED.getDescription());
		}
		if (MappingStatus.ERROR.getCode().equals(mappingCodeDetail.getTransactionStatus())) {
			mappingCodeDetail.setTransactionStatusDisplay(MappingStatus.ERROR.getDescription());
		}
		if (bankCase == null) {
			mappingCodeDetail.setBankCaseDisplay("-");
			mappingCodeDetail.setBankCaseCode("-");
		} else {
			mappingCodeDetail.setBankCaseCode(bankCase.getBankCaseCode());
			mappingCodeDetail.setBankCaseDisplay(bankCase.getBankCaseName());
		}

		if (mappingCodeDetail.getUpdateTime() != null) {
			mappingCodeDetail.setUpdateTimeDisplay(format.format(mappingCodeDetail.getUpdateTime()));
		}

		return mappingCodeDetail;
	}

	@Override
	public MasterMappingCode checkDuplicate(String bankCode, String serviceType) throws Exception {
		MasterMappingCode findMappingCode = null;

		if (serviceType == null) {
			serviceType = "";
		}
		if (bankCode == null) {
			bankCode = "";
		}
		findMappingCode = mappingCodeRepository.findMasterMappingCodeByServiceTypeBankCode(serviceType, bankCode);
		if (findMappingCode != null) {
			throw new Exception("Mapping Code is Duplicated.");
		}

		return findMappingCode;

	}

	@Override
	public MasterBankCase getTransactionStatus(String bankCaseCode) {
		MasterBankCase results = null;
		if (bankCaseCode == null) {
			bankCaseCode = "";
		}
		results = bankCaseRepository.findBybankCaseCode(bankCaseCode);
		if (results == null) {
			return results;
		}
		return results;
	}

}
