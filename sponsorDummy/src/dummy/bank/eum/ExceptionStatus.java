package gec.scf.dummy.bank.eum;

public enum ExceptionStatus {
	AC("AC","Active"),
	IN("IN","Inactive");
	
	private String code ;
	private String description;

	private ExceptionStatus(String code,String description) {
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
