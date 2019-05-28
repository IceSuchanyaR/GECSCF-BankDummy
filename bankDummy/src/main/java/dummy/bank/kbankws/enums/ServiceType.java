package gec.scf.dummy.bank.kbankws.enums;

public enum ServiceType {
	DIRECTDEBIT("DIRECTDEBIT"),
	ENQUIRY_ACCOUNT_BALANCE("ENQUIRY_ACCOUNT_BALANCE"),
	DRAWDOWN("DRAWDOWN"),
	ENQUIRY_CREDIT_LIMIT("ENQUIRY_CREDIT_LIMIT"),
	ENQUIRY_DRAWDOWN("ENQUIRY_DRAWDOWN"),
	ENQUIRY_DIRECT_DEBIT("ENQUIRY_DIRECT_DEBIT");
   
	private String code;

	private ServiceType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
