package gec.scf.dummy.common.exception;


import gec.scf.dummy.common.domain.ErrorItem;

import java.io.Serializable;
import java.util.Collection;

public class MaxMemberCodeLengthException extends InvalidDataException {

    /**
     *
     */
    private static final long serialVersionUID = 4404067157443342647L;

    public MaxMemberCodeLengthException(String module, String event,
                                        Serializable referenceObject, Collection<ErrorItem> errors) {
        super(module, event, referenceObject, errors);
    }

    public MaxMemberCodeLengthException(String event) {
        super(null, event, null, null);
    }

    public MaxMemberCodeLengthException() {
        super(null, null, null, null);
    }

    public Collection<ErrorItem> getErrorFields() {
        return this.getErrors();
    }


}
