package gec.scf.dummy.common.exception;

import java.io.Serializable;
import java.util.Collection;

import gec.scf.dummy.common.domain.ErrorItem;

public class VersionMismatchException extends GECException {

    /**
     *
     */
    private static final long serialVersionUID = 7409530895278963885L;
    private Collection<ErrorItem> errors;

    public VersionMismatchException(String module, String event,
                                    Serializable referenceObject) {
        super(module, event, referenceObject, null);
    }

    public VersionMismatchException(String module, String event, Serializable referenceObject,
                                    Collection<ErrorItem> errors) {

        super(module, event, referenceObject, null);
        this.errors = errors;
    }

    public Collection<ErrorItem> getErrors() {
        return errors;
    }


    public VersionMismatchException(String event) {
        super(null, event, null, null);
    }

    public VersionMismatchException() {
        super(null, null, null, null);
    }


}
