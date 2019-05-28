package dummy.bank.eum;

public enum MasterBankAccountStatus {

	BANK_ACCOUNT_STATUS_ACTIVE("ACTIVE", "Active"), 
	BANK_ACCOUNT_STATUS_SUSPEND("SUSPEND", "Suspend");

	private String code;
	private String description;

	MasterBankAccountStatus(String code, String description) {
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