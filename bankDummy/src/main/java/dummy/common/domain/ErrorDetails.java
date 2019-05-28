package gec.scf.dummy.common.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

public class ErrorDetails implements Serializable {

	public ErrorDetails(LocalDateTime timestamp, String code, String message) {
		super();
		this.code = code;
		this.timestamp = timestamp;
		this.message = message;
	}

	public ErrorDetails(LocalDateTime timestamp, String code, String message,
			Collection<ErrorItem> items) {

		super();
		this.code = code;
		this.timestamp = timestamp;
		this.message = message;
		this.items = items;
	}

	private static final long serialVersionUID = 1L;
	private LocalDateTime timestamp;

	private String code;

	private String message;

	private Collection<ErrorItem> items;

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Collection<ErrorItem> getItems() {
		return items;
	}

	public void setItems(Collection<ErrorItem> items) {
		this.items = items;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
