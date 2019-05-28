package gec.scf.dummy.bank.entity;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "tbl_bank_account")
public class MasterBankAccount extends BaseBankEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id", nullable = false)
	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	private Long accountID;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "bank_code", nullable = false, length = 50)
	private String bankCode;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "account_no", nullable = false, length = 20)
	private String accountNo;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "account_type", nullable = false, length = 20)
	private String accountType;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "credit_limit", nullable = true)
	private BigDecimal creditLimit;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "outstanding", nullable = true)
	private BigDecimal outstanding;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "ledger_balance", nullable = false)
	private BigDecimal ledgerBalance;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "available", nullable = false)
	private BigDecimal available;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "account_status", nullable = false, length = 20)
	private String accountStatus;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "customer_code", nullable = true, length = 20)
	private String customerCode;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "customer_credit_limit", nullable = false)
	private boolean customerCreditLimit;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "customer_id", nullable = false, length = 20)
	private String customerID;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Transient
	private Long itemNo;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Transient
	private String accountTypeDisplay;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Transient
	private String accountStatusDisplay;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Transient
	private String updateTimeDisplay;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Transient
	private String createTimeDisplay;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Transient
	private boolean editMode;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Transient
	private String errorMessage;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Transient
	private String errorMessageStatus;
	
	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "currency_code", nullable = false, length = 10)
	private String currencyCode;

	public MasterBankAccount() {
		super();
	}

	public Long getAccountID() {
		return accountID;
	}

	public void setAccountID(Long accountID) {
		this.accountID = accountID;
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

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public BigDecimal getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
	}

	public BigDecimal getOutstanding() {
		return outstanding;
	}

	public void setOutstanding(BigDecimal outstanding) {
		this.outstanding = outstanding;
	}

	public BigDecimal getLedgerBalance() {
		return ledgerBalance;
	}

	public void setLedgerBalance(BigDecimal ledgerBalance) {
		this.ledgerBalance = ledgerBalance;
	}

	public BigDecimal getAvailable() {
		return available;
	}

	public void setAvailable(BigDecimal available) {
		this.available = available;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public Long getItemNo() {
		return itemNo;
	}

	public void setItemNo(Long itemNo) {
		this.itemNo = itemNo;
	}

	public String getAccountTypeDisplay() {
		return accountTypeDisplay;
	}

	public void setAccountTypeDisplay(String accountTypeDisplay) {
		this.accountTypeDisplay = accountTypeDisplay;
	}

	public String getAccountStatusDisplay() {
		return accountStatusDisplay;
	}

	public void setAccountStatusDisplay(String accountStatusDisplay) {
		this.accountStatusDisplay = accountStatusDisplay;
	}

	public String getUpdateTimeDisplay() {
		return updateTimeDisplay;
	}

	public void setUpdateTimeDisplay(String updateTimeDisplay) {
		this.updateTimeDisplay = updateTimeDisplay;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	
	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public boolean isCustomerCreditLimit() {
		return customerCreditLimit;
	}

	public void setCustomerCreditLimit(boolean customerCreditLimit) {
		this.customerCreditLimit = customerCreditLimit;
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

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

}