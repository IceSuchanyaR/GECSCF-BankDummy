package gec.scf.dummy.bank.kbankws.enums;

public enum LogType {
	DIRECTDEBIT("DIRECTDEBIT"),
	DRAWDOWN("DRAWDOWN"),
	ENQUIRY_ACCOUNT_BALANCE("ENQUIRY_ACCOUNT_BALANCE"),
	ENQUIRY_CREDIT_LIMIT("ENQUIRY_CREDIT_LIMIT"),
	ENQUIRY_DRAWDOWN("ENQUIRY_DRAWDOWN"),
	ENQUIRY_DIRECT_DEBIT("ENQUIRY_DIRECT_DEBIT");
	

	private String code;

	private LogType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
