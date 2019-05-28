package gec.scf.dummy.bank.kbankws.drawdown.domain;


import gec.scf.dummy.bank.kbankws.account.domain.AccountBalanceResponse;
import gec.scf.dummy.bank.kbankws.debit.domain.TransactionStatus;

import java.io.Serializable;

public class DrawdownResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String transactionNo;
	
	private String bankTransactionNo;
	
	private String drawdownTime;

	private TransactionStatus transactionStatus;

	private String failureReasonCode;

	private String failureReason;
	
	private AccountBalanceResponse sourceAccount;
	
	private AccountBalanceResponse sponsorCreditLimitAccount;

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public String getBankTransactionNo() {
		return bankTransactionNo;
	}

	public void setBankTransactionNo(String bankTransactionNo) {
		this.bankTransactionNo = bankTransactionNo;
	}

	public String getDrawdownTime() {
		return drawdownTime;
	}

	public void setDrawdownTime(String drawdownTime) {
		this.drawdownTime = drawdownTime;
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

	public AccountBalanceResponse getSponsorCreditLimitAccount() {
		return sponsorCreditLimitAccount;
	}

	public void setSponsorCreditLimitAccount(AccountBalanceResponse sponsorCreditLimitAccount) {
		this.sponsorCreditLimitAccount = sponsorCreditLimitAccount;
	}

	
	
}
