package gec.scf.dummy.bank.kbankws.credit.limit.service.mock;


import gec.scf.dummy.bank.kbankws.account.domain.AccountBalanceResponse;
import gec.scf.dummy.bank.kbankws.enums.AccountStatus;
import gec.scf.dummy.bank.kbankws.enums.AccountType;
import gec.scf.dummy.bank.kbankws.enums.MappingStatus;

import java.math.BigDecimal;

///////////////////
public final class EnquiryCreditLimitServiceMockBuilder {

	public static EnquiryCreditLimitServiceMockBuilder builder() {
		return new EnquiryCreditLimitServiceMockBuilder();
	}

	private AccountType accountType;

	private String accountNo;

	private MappingStatus enquiryStatus;

	public EnquiryCreditLimitServiceMockBuilder accountNo(String accountNo) {
		this.accountNo = accountNo;
		return this;
	}
	public EnquiryCreditLimitServiceMockBuilder termloan() {
		this.accountType = AccountType.TERM_LOAN;
		return this;
	}

	public EnquiryCreditLimitServiceMockBuilder success() {
		this.enquiryStatus = MappingStatus.SUCCESS;
		return this;
	}

	public AccountBalanceResponse build() {
		AccountBalanceResponse accountBalance = new AccountBalanceResponse();
		accountBalance.setAccountNo(accountNo);
		accountBalance.setAvailableBalance(new BigDecimal("20000.00").toString());
		accountBalance.setAccountStatus(AccountStatus.ACTIVE);
		accountBalance.setAccountType(accountType);
		accountBalance.setCreditLimit(new BigDecimal("20000.00").toString());
		accountBalance.setOutstanding(new BigDecimal("0.00").toString());
		accountBalance.setCurrencyCode("THB");
		accountBalance.setEnquiryStatus(enquiryStatus);
		return accountBalance;
	}

	
}