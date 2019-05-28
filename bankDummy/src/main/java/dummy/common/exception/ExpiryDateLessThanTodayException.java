package gec.scf.dummy.common.exception;

import gec.scf.dummy.common.domain.ErrorItem;

import java.io.Serializable;
import java.util.Collection;

public class ExpiryDateLessThanTodayException extends InvalidDataException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7409530895278963885L;
	private Collection<ErrorItem> errors;

	public ExpiryDateLessThanTodayException(String module, String event,
			Serializable referenceObject, Collection<ErrorItem> errors) {
		super(module, event, referenceObject, null);
	}

	public ExpiryDateLessThanTodayException(String event) {
		super(null, event, null, null);
	}
	public ExpiryDateLessThanTodayException() {
		super(null, null, null, null);
	}

	public Collection<ErrorItem> getErrorFields() {
		return errors;
	}

}
