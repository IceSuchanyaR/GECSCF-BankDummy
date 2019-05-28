package gec.scf.dummy.bank.kbankws.account.service.mock;

import gec.scf.dummy.bank.kbankws.account.domain.AccountBalanceResponse;
import gec.scf.dummy.bank.kbankws.enums.AccountStatus;
import gec.scf.dummy.bank.kbankws.enums.AccountType;
import gec.scf.dummy.bank.kbankws.enums.MappingStatus;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

///////////////////
public final class AccountBalanceMockResponseBuilder {

	public static AccountBalanceMockResponseBuilder builder() {
		return new AccountBalanceMockResponseBuilder();
	}

	private AccountType accountType;
	private String accountNo;
	private MappingStatus enquiryStatus;

	public AccountBalanceMockResponseBuilder accountNo(String accountNo) {
		this.accountNo = accountNo;
		return this;
	}

	public AccountBalanceMockResponseBuilder overdraft() {
		this.accountType = AccountType.OVER_DRAFT;
		return this;
	}

	public AccountBalanceMockResponseBuilder success() {
		this.enquiryStatus = MappingStatus.SUCCESS;
		return this;
	}

	public AccountBalanceResponse build() {
		AccountBalanceResponse accountBalance = new AccountBalanceResponse();
		accountBalance.setAccountNo(accountNo);
		accountBalance.setAccountType(accountType);
		accountBalance.setCustomerId("CUST0001");
		accountBalance.setCreditLimit(new BigDecimal("20000.00").toString());
		accountBalance.setOutstanding(new BigDecimal("0.00").toString());
		accountBalance.setAccountBalance(new BigDecimal("0.00").toString());
		accountBalance.setAvailableBalance(new BigDecimal("20000.00").toString());
		accountBalance.setCurrencyCode("THB");
		accountBalance.setLatestTransactionTime(simpleDateTimeFormat.format(new Date()));
		accountBalance.setAccountStatus(AccountStatus.ACTIVE);
		accountBalance.setEnquiryStatus(enquiryStatus);
		return accountBalance;
	}

	private SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("en"));
}
