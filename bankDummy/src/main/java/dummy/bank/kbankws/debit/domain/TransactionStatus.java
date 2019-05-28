package gec.scf.dummy.bank.kbankws.debit.domain;

public enum TransactionStatus {
	SUCCESS("SUCCESS"), 
	FAILED("FAILED"), 
	ERROR("ERROR");


	private String code;

	private TransactionStatus(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	

}


