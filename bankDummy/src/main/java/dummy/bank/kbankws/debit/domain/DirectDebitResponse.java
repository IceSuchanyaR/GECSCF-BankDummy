package gec.scf.dummy.bank.kbankws.debit.domain;

import gec.scf.dummy.bank.kbankws.account.domain.AccountBalanceResponse;

import java.io.Serializable;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DirectDebitResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String transactionNo;
	
	private String debitTime;

	private String bankTransactionNo;
	
	private TransactionStatus transactionStatus;

	private String failureReasonCode;

	private String failureReason;

	private AccountBalanceResponse sourceAccount;

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public String getDebitTime() {
		return debitTime;
	}

	public void setDebitTime(String debitTime) {
		this.debitTime = debitTime;
	}

	public String getBankTransactionNo() {
		return bankTransactionNo;
	}

	public void setBankTransactionNo(String bankTransactionNo) {
		this.bankTransactionNo = bankTransactionNo;
	}

	public TransactionStatus getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(TransactionStatus transactionStatus) {
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

	public AccountBalanceResponse getSourceAccount() {
		return sourceAccount;
	}

	public void setSourceAccount(AccountBalanceResponse sourceAccount) {
		this.sourceAccount = sourceAccount;
	}

}
