package gec.scf.dummy.bank.kbankws.enums;

public enum MappingStatus {
	SUCCESS("SUCCESS"),
	FAILED("FAILED"),
	ERROR("ERROR");
	
	private String code ;

	private MappingStatus(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
	
	
}
