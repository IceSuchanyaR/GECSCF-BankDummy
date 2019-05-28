package gec.scf.dummy.bank.eum;

public enum BankCaseEnums {
	
	
	
	ENQUIRY_ACCOUNT_SUCCESS("ENQUIRY_ACCOUNT", "ENQUIRY_ACCOUNT_SUCCESS", "Enquiry Account Success","", "SUCCESS"),
	ENQUIRY_ACCOUNT_FAILED_ACC_NOT_REGISTER("ENQUIRY_ACCOUNT", "ENQUIRY_ACCOUNT_FAILED_ACC_NOT_REGISTER", "Account not register","102",	"FAILED"),
	ENQUIRY_ACCOUNT_ERROR_CANNOT_CONNECT("ENQUIRY_ACCOUNT", "ENQUIRY_ACCOUNT_ERROR_CANNOT_CONNECT", "Cannot connect Core Bank","999",	"ERROR"),
	DIRECTDEBIT_SUCCESS("DIRECTDEBIT", "DIRECTDEBIT_SUCCESS", "Payment success" ,"","SUCCESS"),
	DIRECTDEBIT_FAILED_SOURCEACC_NOT_REGISTER("DIRECTDEBIT", "DIRECTDEBIT_FAILED_SOURCEACC_NOT_REGISTER", "SourceAccount not register","102",	"FAILED"),
	DIRECTDEBIT_FAILED_DESTACC_NOT_REGISTER("DIRECTDEBIT", "DIRECTDEBIT_FAILED_DESTACC_NOT_REGISTER", "DestinationAccount not register","102",	"FAILED"),
	DIRECTDEBIT_FAILED_INSUFFICIENT_FUND("DIRECTDEBIT", "DIRECTDEBIT_FAILED_INSUFFICIENT_FUND", "Insufficient funds","101", "FAILED"),
	DIRECTDEBIT_ERROR_CANNOT_CONNECT("DIRECTDEBIT", "DIRECTDEBIT_ERROR_CANNOT_CONNECT", "Cannot connect Core Bank","E99",	"ERROR"),
	DIRECTDEBIT_FAILED_DUPLICATE_TRANSACTION("DIRECTDEBIT", "DIRECTDEBIT_FAILED_DUPLICATE_TRANSACTION", "Duplicate Transaction","E04","FAILED"),
	DIRECTDEBIT_FAILED_ACCSTATUS_NOT_ACTIVE("DIRECTDEBIT", "DIRECTDEBIT_FAILED_ACCSTATUS_NOT_ACTIVE", "Account Status is Suspend","103", "FAILED"),
	DRAWDOWN_SUCCESS("DRAWDOWN", "DRAWDOWN_SUCCESS", "Payment success","","SUCCESS"),
	DRAWDOWN_FAILED_DESTACC_NOT_REGISTER("DRAWDOWN", "DRAWDOWN_FAILED_DESTACC_NOT_REGISTER", "DestinationAccount not register","", "FAILED"),
	DRAWDOWN_FAILED_SOURCEACC_NOT_REGISTER("DRAWDOWN", "DRAWDOWN_FAILED_SOURCEACC_NOT_REGISTER", "SourceAccount not register","", "FAILED"),
	DRAWDOWN_FAILED_INSUFFICIENT_FUND("DRAWDOWN", "DRAWDOWN_FAILED_INSUFFICIENT_FUND", "Insufficient funds","", "FAILED"),
	DRAWDOWN_ERROR_CANNOT_CONNECT("DRAWDOWN", "DRAWDOWN_ERROR_CANNOT_CONNECT", "Cannot connect Core Bank","", "ERROR"),
	ENQUIRY_CREDIT_LIMIT_SUCCESS("ENQUIRY_CREDIT_LIMIT", "ENQUIRY_CREDIT_LIMIT_SUCCESS", "Enquiry Account Success","", "SUCCESS"),
	ENQUIRY_CREDIT_LIMIT_FAILED_ACC_NOT_REGISTER("ENQUIRY_CREDIT_LIMIT", "ENQUIRY_CREDIT_LIMIT_FAILED_ACC_NOT_REGISTER", "Account not register","", "FAILED"),
	ENQUIRY_CREDIT_LIMIT_ERROR_CANNOT_CONNECT("ENQUIRY_CREDIT_LIMIT", "ENQUIRY_CREDIT_LIMIT_ERROR_CANNOT_CONNECT", "Cannot connect Core Bank","", "ERROR");
	

	private String group;
	private String code;
	private String name;
	private String reasonCode; 
	private String status;
	

	

	private BankCaseEnums(String group, String code, String name, String reasonCode, String status) {
		this.group = group;
		this.code = code;
		this.name = name;
		this.reasonCode = reasonCode;
		this.status = status;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}	
}