package gec.scf.dummy.common.exception;

import java.io.Serializable;
import java.util.Collection;

import gec.scf.dummy.common.domain.ErrorItem;

public class InvalidDataException extends GECException {

    private static final long serialVersionUID = 1L;
    private Collection<ErrorItem> errors;

    public InvalidDataException(String module, String event, Serializable referenceObject, Collection<ErrorItem> errors) {

        super(module, event, referenceObject, null);
        this.errors = errors;
    }

    public InvalidDataException(String event) {
        super(null, event, null, null);
    }

    public Collection<ErrorItem> getErrors() {
        return errors;
    }

    public InvalidDataException() {
        super(null, null, null, null);
    }

}
