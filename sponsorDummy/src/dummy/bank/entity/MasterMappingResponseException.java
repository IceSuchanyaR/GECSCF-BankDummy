package gec.scf.dummy.bank.entity;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;

@Entity
@Table(name = "tbl_mapping_response_exception")
public class MasterMappingResponseException extends BaseBankEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exception_id", nullable = false)
    @JsonView({BankManageEvent.View.DetailForManageBank.class})
    private Long exceptionID;


    @Column(name = "bank_code", nullable = false, length = 50)
    @JsonView({BankManageEvent.View.DetailForManageBank.class})
    private String bankCode;

    @Column(name = "account_no", nullable = false, length = 20)
    @JsonView({BankManageEvent.View.DetailForManageBank.class})
    private String accountNo;

    @Column(name = "service_type", nullable = false, length = 50)
    @JsonView({BankManageEvent.View.DetailForManageBank.class})
    private String serviceType;

    @Column(name = "delay", nullable = false)
    @JsonView({BankManageEvent.View.DetailForManageBank.class})
    private int delay;

    @Column(name = "transaction_status", nullable = false, length = 20)
    @JsonView({BankManageEvent.View.DetailForManageBank.class})
    private String transactionStatus;

    @Column(name = "failure_reason_code", nullable = true, length = 255)
    @JsonView({BankManageEvent.View.DetailForManageBank.class})
    private String failureReasonCode;

    @Column(name = "failure_reason", nullable = true, length = 255)
    @JsonView({BankManageEvent.View.DetailForManageBank.class})
    private String failureReason;

    @Column(name = "status", nullable = false)
    @JsonView({BankManageEvent.View.DetailForManageBank.class})
    private String status;

    @Transient
    @JsonView({BankManageEvent.View.DetailForManageBank.class})
    private Long itemNo;

    @Transient
    @JsonView({BankManageEvent.View.DetailForManageBank.class})
    private String transactionStatusDisplay;

    @Transient
    @JsonView({BankManageEvent.View.DetailForManageBank.class})
    private String statusDisplay;

    @Transient
    @JsonView({BankManageEvent.View.DetailForManageBank.class})
    private String updateTimeDisplay;

    @Transient
    @JsonView({BankManageEvent.View.DetailForManageBank.class})
    private String createTimeDisplay;

    @Transient
    @JsonView({BankManageEvent.View.DetailForManageBank.class})
    private boolean editMode;

    @Transient
    @JsonView({BankManageEvent.View.DetailForManageBank.class})
    private String errorMessage;

    @Transient
    @JsonView({BankManageEvent.View.DetailForManageBank.class})
    private String errorMessageStatus;

    @Transient
    @JsonView({BankManageEvent.View.DetailForManageBank.class})
    private String serviceTypeDisplay;


    public Long getExceptionID() {
        return exceptionID;
    }

    public void setExceptionID(Long exceptionID) {
        this.exceptionID = exceptionID;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }


    public String getStatusDisplay() {
        return statusDisplay;
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getFailureReasonCode() {
        return failureReasonCode;
    }

    public void setFailureReasonCode(String failureReasonCode) {
        this.failureReasonCode = failureReasonCode;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getItemNo() {
        return itemNo;
    }

    public void setItemNo(Long itemNo) {
        this.itemNo = itemNo;
    }

    public String getServiceTypeDisplay() {
        return serviceTypeDisplay;
    }

    public void setServiceTypeDisplay(String serviceTypeDisplay) {
        this.serviceTypeDisplay = serviceTypeDisplay;
    }

    public String getUpdateTimeDisplay() {
        return updateTimeDisplay;
    }

    public void setUpdateTimeDisplay(String updateTimeDisplay) {
        this.updateTimeDisplay = updateTimeDisplay;
    }

    public String getCreateTimeDisplay() {
        return createTimeDisplay;
    }

    public void setCreateTimeDisplay(String createTimeDisplay) {
        this.createTimeDisplay = createTimeDisplay;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessageStatus() {
        return errorMessageStatus;
    }

    public void setErrorMessageStatus(String errorMessageStatus) {
        this.errorMessageStatus = errorMessageStatus;
    }

    public String getTransactionStatusDisplay() {
        return transactionStatusDisplay;
    }

    public void setTransactionStatusDisplay(String transactionStatusDisplay) {
        this.transactionStatusDisplay = transactionStatusDisplay;
    }

}
