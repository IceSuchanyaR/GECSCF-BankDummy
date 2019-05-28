package dummy.bank.eum;

public enum LogTransactionMode {

	DIRECT_DEBIT("DIRECT_DEBIT","Direct Debit"),
	ENQUIRY_DIRECT_DEBIT("ENQUIRY_DIRECT_DEBIT","Enquiry Direct Debit"),
	ENQUIRY_ACCOUNT_BALANCE("ENQUIRY_ACCOUNT_BALANCE","Enquiry Account Balance"),
	DRAWDOWN("DRAWDOWN","Drawdown"),
	ENQUIRY_DRAWDOWN("ENQUIRY_DRAWDOWN","Enquiry Drawdown"),
	ENQUIRY_CREDIT_LIMIT("ENQUIRY_CREDIT_LIMIT","Enquiry Credit Limit");


	private String code;
	private String description;

	LogTransactionMode(String code, String description) {
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
