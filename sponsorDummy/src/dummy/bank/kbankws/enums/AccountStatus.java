package gec.scf.dummy.bank.kbankws.enums;

public enum AccountStatus {
	ACTIVE("ACTIVE"),
	SUSPEND("SUSPEND");
	
	private String code;

	private AccountStatus(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
