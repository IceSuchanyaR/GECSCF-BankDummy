package gec.scf.dummy.common.exception;


import gec.scf.dummy.common.domain.ErrorItem;

import java.io.Serializable;
import java.util.Collection;

public class RequestIsInProcessingException extends GECException {

    /**
     *
     */
    private static final long serialVersionUID = 6722433389768458046L;

    private Collection<ErrorItem> errors;

    public RequestIsInProcessingException() {
        super(null, null, null, null);
    }

    public RequestIsInProcessingException(String module, String event,
                                          Serializable referenceObject,
                                          Collection<ErrorItem> errors) {
        super(module, event, referenceObject, null);
        this.errors = errors;
    }

    public RequestIsInProcessingException(String event) {
        super(null, event, null, null);
    }

    public Collection<ErrorItem> getErrors() {
        return errors;
    }

}
