package gec.scf.dummy.bank.controller;

import com.fasterxml.jackson.annotation.JsonView;
import gec.scf.dummy.bank.entity.ConfigApplication;
import gec.scf.dummy.bank.entity.BankManageEvent;
import gec.scf.dummy.bank.entity.MasterMappingResponseException;
import gec.scf.dummy.bank.service.mappingexception.MappingExceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/mappingException")
public class MappingExceptionController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private MappingExceptionService mappingExceptionService;
    @JsonView({BankManageEvent.View.DetailForManageBank.class})
    @RequestMapping(value = "/mappingExceptionList", method = RequestMethod.GET)
    public List<MasterMappingResponseException> getAllMappingException(
            @RequestParam(value = "serviceType", required = false) String serviceType,
            @RequestParam(value = "bankCode", required = false) String bankCode,
            @RequestParam(value = "accountNo", required = false) String accountNo,
            @RequestParam(value = "itemNo", required = false) Long itemNo) {
        List<MasterMappingResponseException> results = null;
        try {
            results = mappingExceptionService.getAllMappingException(serviceType, bankCode, accountNo, itemNo);
        } catch (Exception e) {
            results = new ArrayList<>();
        }
        return results;
    }

    @RequestMapping(value = "/totalMappingException", method = RequestMethod.GET)
    public Long getTotalMappingException(@RequestParam(value = "serviceType", required = false) String serviceType,
                                         @RequestParam(value = "bankCode", required = false) String bankCode,
                                         @RequestParam(value = "accountNo", required = false) String accountNo) throws Exception {
        return mappingExceptionService.getTotalMappingException(serviceType, bankCode, accountNo);
    }

    @JsonView({BankManageEvent.View.DetailForManageBank.class})
    @RequestMapping(value = "/deleteMappingException", method = RequestMethod.DELETE)
    public MasterMappingResponseException deleteMappingException(@RequestParam(value = "exceptionID", required = false) Long exceptionID) {
        MasterMappingResponseException findMappingException = null;

        try {
            findMappingException = mappingExceptionService.findMasterMappingExceptionByexceptionID(exceptionID);

            if (findMappingException == null) {
                throw new Exception("MappingException is Not Found.");
            }
            mappingExceptionService.deleteMappingException(findMappingException);
        } catch (Exception e) {
            findMappingException = new MasterMappingResponseException();
            findMappingException.setErrorMessage("Delete MappingException Fail  :  " + e.getMessage());
            findMappingException.setErrorMessageStatus("FAIL");
        }
        return findMappingException;
    }

    @JsonView({BankManageEvent.View.DetailForManageBank.class})
    @RequestMapping(value = "/getServiceType", method = RequestMethod.GET)
    public List<ConfigApplication> getAllServiceType() throws Exception {
        List<ConfigApplication> results = null;

        try {
            results = mappingExceptionService.getAllServiceType();

        } catch (Exception e) {
            results = new ArrayList<>();
        }
        return results;
    }

    @JsonView({BankManageEvent.View.DetailForManageBank.class})
    @RequestMapping(value = "/mappingExceptionView", method = RequestMethod.GET)
    public MasterMappingResponseException getMappingExceptionByExceptionID(
            @RequestParam(value = "exceptionID", required = false) Long exceptionID) {
        MasterMappingResponseException result = null;
        try {
            result = mappingExceptionService.findMasterMappingExceptionByexceptionID(exceptionID);
        } catch (Exception e) {
            result = new MasterMappingResponseException();
        }
        return result;
    }

    @JsonView({BankManageEvent.View.DetailForManageBank.class})
    @RequestMapping(value = "/manageMappingException", method = RequestMethod.POST)
    public MasterMappingResponseException manageMappingException(@RequestBody MasterMappingResponseException masterMappingException) {
        MasterMappingResponseException masterMappingExceptionResponse = new MasterMappingResponseException();
        try {
            if (masterMappingException.isEditMode()) {
                masterMappingExceptionResponse = mappingExceptionService.editMappingException(masterMappingException);
            } else {
                masterMappingExceptionResponse = mappingExceptionService.newMappingException(masterMappingException);
            }
            if (masterMappingExceptionResponse == null) {
                masterMappingExceptionResponse = new MasterMappingResponseException();
            }
        } catch (Exception e) {
            masterMappingExceptionResponse.setErrorMessage("Submit Mapping Response Exception Fail  :  " + e.getMessage());
            masterMappingExceptionResponse.setErrorMessageStatus("FAIL");
        }

        System.out.println(masterMappingExceptionResponse.toString());
        System.out.println(masterMappingExceptionResponse.toString());
        System.out.println(masterMappingExceptionResponse.toString());
        System.out.println(masterMappingExceptionResponse.toString());
        System.out.println(masterMappingExceptionResponse.toString());
        System.out.println(masterMappingExceptionResponse.toString());
        System.out.println(masterMappingExceptionResponse.toString());
        return masterMappingExceptionResponse;
    }

    private boolean isValidMappingException(MasterMappingResponseException masterMappingException) throws Exception {
        boolean result = true;
        if (masterMappingException == null) {
            throw new Exception("MasterMappingResponseException cannot be Null.");
        }
        if (masterMappingException.getBankCode() == null || "".equals(masterMappingException.getBankCode())) {
            throw new Exception("BankCode is Null.");
        }
        if (masterMappingException.getBankCode().length() > 50) {
            throw new Exception("BankCode must no more than 50 characters.");
        }
        if (masterMappingException.getAccountNo() == null || "".equals(masterMappingException.getAccountNo())) {
            throw new Exception("AccountNo is Null.");
        }
        if (masterMappingException.getAccountNo().length() > 20) {
            throw new Exception("AccountNo must no more than 20 characters.");
        }

        if (masterMappingException.getServiceType() == null || "".equals(masterMappingException.getServiceType())) {
            throw new Exception("ServiceType is Null.");
        }
        if (masterMappingException.getServiceType().length() > 50) {
            throw new Exception("ServiceType must no more than 50 characters.");
        }

        if (masterMappingException.getFailureReason() != null && masterMappingException.getFailureReason().length() > 255) {
            throw new Exception("Failure Reason must no more than 50 characters.");
        }

        if (masterMappingException.getFailureReasonCode() != null && masterMappingException.getFailureReasonCode().length() > 255) {
            throw new Exception("Failure Reason Code must no more than 255 characters.");
        }
        return result;
    }
}
