package gec.scf.dummy.common.exception;

import java.io.Serializable;
import java.util.Collection;

import gec.scf.dummy.common.domain.ErrorItem;

public class DuplicationException extends GECException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7409530895278963885L;
	private Collection<ErrorItem> errors;

	public DuplicationException(String module, String event, Serializable referenceObject,
			Collection<ErrorItem> errors) {
		super(module, event, referenceObject, null);
		this.errors = errors;
	}

	public DuplicationException() {
		super(null, null, null, null);
	}

	public DuplicationException(String event) {
		super(null, event, null, null);
	}
	public Collection<ErrorItem> getErrorFields() {
		return errors;
	}

}
