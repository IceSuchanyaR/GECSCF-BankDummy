package gec.scf.dummy.common.domain;

import java.io.Serializable;

import org.springframework.validation.FieldError;

public class ErrorItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	private String code;

	private Serializable rejectedValue;

	public ErrorItem(FieldError fieldError) {
		this.name = fieldError.getField();
		this.code = fieldError.getDefaultMessage();

		if (fieldError.getRejectedValue() instanceof Serializable) {
			this.rejectedValue = (Serializable) fieldError.getRejectedValue();
		}
		else {
			this.rejectedValue = String.valueOf(fieldError.getRejectedValue());
		}
	}

	public ErrorItem(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getRejectedValue() {
		return rejectedValue;
	}

	public void setRejectedValue(Serializable rejectedValue) {
		this.rejectedValue = rejectedValue;
	}

}
