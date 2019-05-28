package gec.scf.dummy.bank.kbankws.enums;

public enum AccountType {
	CURRENT("CURRENT"), 
	SAVING("SAVING"), 
	OVER_DRAFT("OVER_DRAFT"), 
	TERM_LOAN("TERM_LOAN");

	private String code;
	

	private AccountType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	

}
