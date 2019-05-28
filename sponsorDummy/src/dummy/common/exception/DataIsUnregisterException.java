package gec.scf.dummy.common.exception;


import gec.scf.dummy.common.domain.ErrorItem;

import java.io.Serializable;
import java.util.Collection;

public class DataIsUnregisterException extends GECRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7507954894473417571L;
	private Collection<ErrorItem> errors;


	public DataIsUnregisterException() {
		super(null, null, null, null);
	}

	public DataIsUnregisterException(String module, String event,
									 Serializable referenceObject) {
		super(module, event, referenceObject, null);
	}

	public DataIsUnregisterException(String module, String event, Serializable referenceObject,
									 Collection<ErrorItem> errors) {

		super(module, event, referenceObject, null);
		this.errors = errors;
	}

	public DataIsUnregisterException(String event) {
		super(null, event, null, null);
	}
	public Collection<ErrorItem> getErrors() {
		return errors;
	}
}
