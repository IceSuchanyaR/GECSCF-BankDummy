package gec.scf.dummy.common.exception;

import java.io.Serializable;
import java.util.Collection;

import gec.scf.dummy.common.domain.ErrorItem;

public class DataIsInUseException extends GECException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7409530895278963885L;
	private Collection<ErrorItem> errors;

	public DataIsInUseException() {
		super(null, null, null, null);
	}
	public DataIsInUseException(String module, String event,
			Serializable referenceObject) {
		super(module, event, referenceObject, null);
	}

	public DataIsInUseException(String module, String event, Serializable referenceObject,
									Collection<ErrorItem> errors) {

		super(module, event, referenceObject, null);
		this.errors = errors;
	}

	public DataIsInUseException(String event) {
		super(null, event, null, null);
	}
	public Collection<ErrorItem> getErrors() {
		return errors;
	}
}
