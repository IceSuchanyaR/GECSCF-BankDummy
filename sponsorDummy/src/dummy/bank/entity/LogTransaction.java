package gec.scf.dummy.bank.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "tbl_log_transaction")
public class LogTransaction implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_id", nullable = false)
	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	private Long logID;

	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	@Column(name = "log_type", nullable = false, length = 50)
	private String logType;

	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	@Column(name = "transaction_no", nullable = true, length = 50)
	private String transactionNo;

	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	@Column(name = "bank_code", nullable = true, length = 50)
	private String bankCode;

	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	@Column(name = "sponsor_code", nullable = true, length = 20)
	private String sponsorCode;

	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	@Column(name = "sponsor_credit_account_no", nullable = true, length = 20)
	private String sponsorCreditAccountNo;

	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	@Column(name = "sponsor_credit_limit_customer_id", nullable = true, length = 20)
	private String sponsorCreditLimitCustomerID;

	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	@Column(name = "sponsor_credit_limit_account_type", nullable = true, length = 20)
	private String sponsorCreditLimitAccountType;

	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	@Column(name = "source_customer_id", nullable = true, length = 20)
	private String sourceCustomerID;

	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	@Column(name = "source_account_type", nullable = true, length = 20)
	private String sourceAccountType;

	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	@Column(name = "source_account_no", nullable = true, length = 20)
	private String sourceAccountNo;

	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	@Column(name = "destination_customer_id", nullable = true, length = 20)
	private String destinationCustomerID;

	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	@Column(name = "destination_account_type", nullable = true, length = 20)
	private String destinationAccountType;

	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	@Column(name = "destination_account_no", nullable = true, length = 20)
	private String destinationAccountNo;

	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	@Temporal(TemporalType.DATE)
	@Column(name = "transaction_date", nullable = true)
	private Date transactionDate;

	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	@Column(name = "transaction_amount", nullable = true, length = 20)
	private String transactionAmount;

	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	@Column(name = "currency_code", nullable = true, length = 3)
	private String currencyCode;

	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	@Column(name = "transaction_status", nullable = false, length = 20)
	private String transactionStatus;

	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	@Column(name = "failure_reason_code", nullable = true, length = 255)
	private String failureReasonCode;

	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	@Column(name = "failure_reason", nullable = true, length = 255)
	private String failureReason;

	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	@Column(name = "maturity_date", nullable = true, length = 10)
	private String maturityDate;

	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	@Column(name = "document_amount", nullable = true, length = 20)
	private String documentAmount;

	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	@Column(name = "finance_percent", nullable = true, length = 20)
	private String financePercent;

	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	@Column(name = "bank_transaction_no", nullable = true, length = 50)
	private String bankTransactionNo;

	@Transient
	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	private Long itemNo;

	@Transient
	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	private String logTypeDisplay;

	@Transient
	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	private String sponsorCreditLimitAccountTypeDisplay;

	@Transient
	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	private String sourceAccountTypeDisplay;

	@Transient
	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	private String destinationAccountTypeDisplay;

	@Transient
	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	private String transactionStatusDisplay;

	@Transient
	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	private String updateTimeDisplay;

	@Transient
	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	private String transactionDateDisplay;

	@Transient
	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	private String createTimeDisplay;

	@Transient
	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	private boolean editMode;

	@Transient
	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	private String errorMessage;

	@Transient
	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	private String errorMessageStatus;

	@Transient
	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	private String failureReasonCodeView;

	@Transient
	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	private String failureReasonView;

	@Transient
	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	private String bankTransactionNoView;

	@JsonView({ BankManageEvent.View.DetailForManageBank.class })
	private Date updateTime;

	public Long getLogID() {
		return logID;
	}

	public void setLogID(Long logID) {
		this.logID = logID;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String sourceBankCode) {
		this.bankCode = sourceBankCode;
	}

	public String getSourceCustomerID() {
		return sourceCustomerID;
	}

	public void setSourceCustomerID(String sourceCustomerID) {
		this.sourceCustomerID = sourceCustomerID;
	}

	public String getSourceAccountType() {
		return sourceAccountType;
	}

	public void setSourceAccountType(String sourceAccountType) {
		this.sourceAccountType = sourceAccountType;
	}

	public String getSourceAccountNo() {
		return sourceAccountNo;
	}

	public void setSourceAccountNo(String sourceAccountNo) {
		this.sourceAccountNo = sourceAccountNo;
	}

	public String getSponsorCode() {
		return sponsorCode;
	}

	public void setSponsorCode(String sponsorCode) {
		this.sponsorCode = sponsorCode;
	}

	public String getSponsorCreditAccountNo() {
		return sponsorCreditAccountNo;
	}

	public void setSponsorCreditAccountNo(String sponsorCreditAccountNo) {
		this.sponsorCreditAccountNo = sponsorCreditAccountNo;
	}

	public String getSponsorCreditLimitCustomerID() {
		return sponsorCreditLimitCustomerID;
	}

	public void setSponsorCreditLimitCustomerID(String sponsorCreditLimitCustomerID) {
		this.sponsorCreditLimitCustomerID = sponsorCreditLimitCustomerID;
	}

	public String getSponsorCreditLimitAccountType() {
		return sponsorCreditLimitAccountType;
	}

	public void setSponsorCreditLimitAccountType(String sponsorCreditLimitAccountType) {
		this.sponsorCreditLimitAccountType = sponsorCreditLimitAccountType;
	}

	public String getDestinationCustomerID() {
		return destinationCustomerID;
	}

	public void setDestinationCustomerID(String destinationCustomerID) {
		this.destinationCustomerID = destinationCustomerID;
	}

	public String getDestinationAccountType() {
		return destinationAccountType;
	}

	public void setDestinationAccountType(String destinationAccountType) {
		this.destinationAccountType = destinationAccountType;
	}

	public String getDestinationAccountNo() {
		return destinationAccountNo;
	}

	public void setDestinationAccountNo(String destinationAccountNo) {
		this.destinationAccountNo = destinationAccountNo;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
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

	public String getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(String maturityDate) {
		this.maturityDate = maturityDate;
	}

	public String getDocumentAmount() {
		return documentAmount;
	}

	public void setDocumentAmount(String documentAmount) {
		this.documentAmount = documentAmount;
	}

	public String getFinancePercent() {
		return financePercent;
	}

	public void setFinancePercent(String financePercent) {
		this.financePercent = financePercent;
	}

	public String getBankTransactionNo() {
		return bankTransactionNo;
	}

	public void setBankTransactionNo(String bankTransactionNo) {
		this.bankTransactionNo = bankTransactionNo;
	}

	public Long getItemNo() {
		return itemNo;
	}

	public void setItemNo(Long itemNo) {
		this.itemNo = itemNo;
	}

	public String getLogTypeDisplay() {
		return logTypeDisplay;
	}

	public void setLogTypeDisplay(String logTypeDisplay) {
		this.logTypeDisplay = logTypeDisplay;
	}

	public String getSponsorCreditLimitAccountTypeDisplay() {
		return sponsorCreditLimitAccountTypeDisplay;
	}

	public void setSponsorCreditLimitAccountTypeDisplay(String sponsorCreditLimitAccountTypeDisplay) {
		this.sponsorCreditLimitAccountTypeDisplay = sponsorCreditLimitAccountTypeDisplay;
	}

	public String getSourceAccountTypeDisplay() {
		return sourceAccountTypeDisplay;
	}

	public void setSourceAccountTypeDisplay(String sourceAccountTypeDisplay) {
		this.sourceAccountTypeDisplay = sourceAccountTypeDisplay;
	}

	public String getDestinationAccountTypeDisplay() {
		return destinationAccountTypeDisplay;
	}

	public void setDestinationAccountTypeDisplay(String destinationAccountTypeDisplay) {
		this.destinationAccountTypeDisplay = destinationAccountTypeDisplay;
	}

	public String getTransactionStatusDisplay() {
		return transactionStatusDisplay;
	}

	public void setTransactionStatusDisplay(String transactionStatusDisplay) {
		this.transactionStatusDisplay = transactionStatusDisplay;
	}

	public String getUpdateTimeDisplay() {
		return updateTimeDisplay;
	}

	public void setUpdateTimeDisplay(String updateTimeDisplay) {
		this.updateTimeDisplay = updateTimeDisplay;
	}

	public String getTransactionDateDisplay() {
		return transactionDateDisplay;
	}

	public void setTransactionDateDisplay(String transactionDateDisplay) {
		this.transactionDateDisplay = transactionDateDisplay;
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

	public String getFailureReasonCodeView() {
		return failureReasonCodeView;
	}

	public void setFailureReasonCodeView(String failureReasonCodeView) {
		this.failureReasonCodeView = failureReasonCodeView;
	}

	public String getFailureReasonView() {
		return failureReasonView;
	}

	public void setFailureReasonView(String failureReasonView) {
		this.failureReasonView = failureReasonView;
	}

	public String getBankTransactionNoView() {
		return bankTransactionNoView;
	}

	public void setBankTransactionNoView(String bankTransactionNoView) {
		this.bankTransactionNoView = bankTransactionNoView;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;

	}

}
