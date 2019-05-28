package gec.scf.dummy.bank.kbankws.enums;

public enum CurrencyCode {
	THB("THB", "Baht", "THAILAND"),
	LAK("LAK","Kip","LAO PEOPLE’S DEMOCRATIC REPUBLIC (THE)"),
	KHR("KHR","Riel","CAMBODIA");

	private String code;
	private String currency;
	private String country;
	

	private CurrencyCode(String code, String currency, String country) {
		this.code = code;
		this.currency = currency;
		this.country = country;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	

}
