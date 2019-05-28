package dummy.bank.eum;

public enum MasterBankAccountType {
	
	BANK_ACCOUNT_TYPE_CURRENT("CURRENT", "Current"),
	BANK_ACCOUNT_TYPE_SAVING("SAVING", "Saving"),
	BANK_ACCOUNT_TYPE_OVERDRAFT("OVER_DRAFT","Over Draft"),
	BANK_ACCOUNT_TYPE_DRAWDOWN("TERM_LOAN","Term Loan");

	private String code;
	private String description;

	MasterBankAccountType(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
