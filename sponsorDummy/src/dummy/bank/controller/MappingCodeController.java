package gec.scf.dummy.bank.controller;

import com.fasterxml.jackson.annotation.JsonView;
import gec.scf.dummy.bank.entity.*;
import gec.scf.dummy.bank.service.mappingcode.MappingCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("hasAnyAuthority('MANAGE_BANK','MANAGE_ALL')")
@RequestMapping(value = "/mappingCode")
public class MappingCodeController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private MappingCodeService mappingCodeService;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@RequestMapping(value = "/mappingCodeList", method = RequestMethod.GET)
	public List<MasterMappingCode> getAllMappingCode(
			@RequestParam(value = "serviceType", required = false) String serviceType,
			@RequestParam(value = "bankCode", required = false) String bankCode,
			@RequestParam(value = "itemNo", required = false) Long itemNo) {
		List<MasterMappingCode> results = null;
		try {
			results = mappingCodeService.getAllMappingCode(serviceType, bankCode, itemNo);
		} catch (Exception e) {
			results = new ArrayList<>();
		}
		return results;
	}

	@RequestMapping(value = "/totalMappingCode", method = RequestMethod.GET)
	public Long getTotalMappingCode(@RequestParam(value = "serviceType", required = false) String serviceType,
			@RequestParam(value = "bankCode", required = false) String bankCode) throws Exception {
		return mappingCodeService.getTotalMappingCode(serviceType, bankCode);
	}

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@RequestMapping(value = "/deleteMappingCode", method = RequestMethod.DELETE)
	public MasterMappingCode deleteMappingCode(@RequestParam(value = "mappingID", required = false) Long mappingID) {
		MasterMappingCode findMappingCode = null;

		try {
			findMappingCode = mappingCodeService.findMasterMappingCodeBymappingID(mappingID);

			if (findMappingCode == null) {
				throw new Exception("MappingCode is Not Found.");
			}
			mappingCodeService.deleteMappingCode(findMappingCode);
		} catch (Exception e) {
			findMappingCode = new MasterMappingCode();
			findMappingCode.setErrorMessage("Delete MappingCode Fail  :  " + e.getMessage());
			findMappingCode.setErrorMessageStatus("FAIL");
		}
		return findMappingCode;
	}

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@RequestMapping(value = "/getServiceType", method = RequestMethod.GET)
	public List<ConfigApplication> getAllServiceType() throws Exception {
		List<ConfigApplication> results = null;

		try {
			results = mappingCodeService.getAllServiceType();

		} catch (Exception e) {
			results = new ArrayList<>();
		}
		return results;
	}

	@RequestMapping(value = "/getBankCode", method = RequestMethod.GET)
	public List<String> getAllBankCode() {
		List<String> results = null;
		try {
			results = mappingCodeService.getBankCode();
		} catch (Exception e) {
			results = new ArrayList<>();
		}
		return results;
	}

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@RequestMapping(value = "/mappingCodeView", method = RequestMethod.GET)
	public MasterMappingCode getMappingCodeByMappingID(
			@RequestParam(value = "mappingID", required = false) Long mappingID) {
		MasterMappingCode result = null;
		try {
			result = mappingCodeService.findMasterMappingCodeBymappingID(mappingID);
		} catch (Exception e) {
			result = new MasterMappingCode();
		}
		return result;
	}
	


	@RequestMapping(value = "/totalMappingCodeDetail", method = RequestMethod.GET)
	public Long getTotalMappingCodeDetail(@RequestParam(value = "mappingID", required = false) Long mappingID)
			throws Exception {
		return mappingCodeService.getTotalMappingCodeDetail(mappingID);
	}

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@RequestMapping(value = "/getMasterBankCase", method = RequestMethod.GET)
	public List<MasterBankCase> getAllMasterBankCase(
			@RequestParam(value = "serviceType", required = false) String serviceType) {
		List<MasterBankCase> results = null;
		
		try {
			results = mappingCodeService.getAllMasterBankCase(serviceType);
		} catch (Exception e) {
			results = new ArrayList<>();
		}
		return results;
	}

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@RequestMapping(value = "/checkDuplicate", method = RequestMethod.GET)
	public MasterMappingCode checkDuplicate(@RequestParam(value = "bankCode", required = false) String bankCode,
			@RequestParam(value = "serviceType", required = false) String serviceType) {
		MasterMappingCode masterMappingCode = new MasterMappingCode();
		try {
			if (isValidMappingCode(bankCode, serviceType)) {
				masterMappingCode = mappingCodeService.checkDuplicate(bankCode, serviceType);
			}
			if (masterMappingCode == null) {
				masterMappingCode = new MasterMappingCode();
			}
		} catch (Exception e) {
			masterMappingCode.setErrorMessage("Mapping Response Bank Fail  :  " + e.getMessage());
			masterMappingCode.setErrorMessageStatus("FAIL");
		}
		return masterMappingCode;

	}

	private boolean isValidMappingCode(String bankCode, String serviceType) throws Exception {
		boolean result = true;
		if (bankCode == null) {
			throw new Exception("BankCode is Null.");
		}
		if (bankCode.length() > 50) {
			throw new Exception("BankCode must no more than 50 digits.");
		}
		if (serviceType == null) {
			throw new Exception("ServiceType is Null.");
		}
		return result;
	}

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@RequestMapping(value = "/getTransactionStatus", method = RequestMethod.GET)
	public MasterBankCase getTransactionStatus(
			@RequestParam(value = "bankCaseDisplay", required = false) String bankCaseDisplay) {
		MasterBankCase results = null;
		try {
			results = mappingCodeService.getTransactionStatus(bankCaseDisplay);
		} catch (Exception e) {
			results = new MasterBankCase();
		}
		return results;
	}

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@RequestMapping(value = "/manageMappingCode", method = RequestMethod.POST)
	public MasterMappingCode manageMappingCode(@RequestBody MasterMappingCode masterMappingCode) {
		MasterMappingCode masterMappingCodeResponse = new MasterMappingCode();
		try {
			// valid
			if (isValidMappingCode(masterMappingCode)) {
				// Prepare Mapping Code Detail
				MasterMappingCode masterMappingCodeItem = preapreMappingCodeDetail(masterMappingCode);
				if (masterMappingCode.isEditMode()) {
					masterMappingCodeResponse = mappingCodeService.editMappingCode(masterMappingCodeItem);
				} else {
					masterMappingCodeResponse = mappingCodeService.newMappingCode(masterMappingCodeItem);
				}
				if (masterMappingCodeResponse == null) {
					masterMappingCodeResponse = new MasterMappingCode();
				}
			}
		} catch (Exception e) {
			masterMappingCodeResponse.setErrorMessage("Submit Mapping Code Fail  :  " + e.getMessage());
			masterMappingCodeResponse.setErrorMessageStatus("FAIL");
		}
		return masterMappingCodeResponse;
	}

	private MasterMappingCode preapreMappingCodeDetail(MasterMappingCode masterMappingCode) throws Exception {
		try {
			if (masterMappingCode != null && !masterMappingCode.getMasterMappingCodeDetail().isEmpty()) {
				List<MasterBankCase> getBankCases = mappingCodeService
						.getAllMasterBankCase(masterMappingCode.getServiceType());
				if (getBankCases.isEmpty()) {
					throw new Exception("Mapping Response Bank : Bank Case is not found.");
				}

				for (MasterMappingCodeDetail each : masterMappingCode.getMasterMappingCodeDetail()) {
					for (MasterBankCase bankCase : getBankCases) {
						if (each.getBankCaseDisplay().equals(bankCase.getBankCaseName())) {
							each.setBankCaseCode(bankCase.getBankCaseCode());
							continue;
						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return masterMappingCode;
	}

	private boolean isValidMappingCode(MasterMappingCode masterMappingCode) throws Exception {
		boolean result = true;
		if (masterMappingCode == null) {
			throw new Exception("MasterBankAccount is Null.");
		}
		if (masterMappingCode.getBankCode() == null || "".equals(masterMappingCode.getBankCode())) {
			throw new Exception("BankCode is Null.");
		}
		if (masterMappingCode.getBankCode().length() > 50) {
			throw new Exception("BankCode must no more than 50 characters.");
		}
		if (masterMappingCode.getServiceType() == null || "".equals(masterMappingCode.getServiceType())) {
			throw new Exception("ServiceType is Null.");
		}
		if (masterMappingCode.getServiceType().length() > 50) {
			throw new Exception("ServiceType must no more than 50 characters.");
		}
		if(masterMappingCode.getMasterMappingCodeDetail()  == null ) {
			throw new Exception("MasterMappingCodeDetail is Null.");
		}
		return result;
	}

}
