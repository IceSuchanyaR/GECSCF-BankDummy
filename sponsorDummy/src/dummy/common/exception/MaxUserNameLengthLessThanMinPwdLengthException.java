package gec.scf.dummy.common.exception;


import gec.scf.dummy.common.domain.ErrorItem;

import java.io.Serializable;
import java.util.Collection;

public class MaxUserNameLengthLessThanMinPwdLengthException extends InvalidDataException {

    /**
     *
     */
    private static final long serialVersionUID = 4404067157443342647L;
    private Collection<ErrorItem> errors;

    public MaxUserNameLengthLessThanMinPwdLengthException(String module, String event,
                                                          Serializable referenceObject, Collection<ErrorItem> errors) {
        super(module, event, referenceObject, null);
    }

    public MaxUserNameLengthLessThanMinPwdLengthException(String event) {
        super(null, event, null, null);
    }

    public MaxUserNameLengthLessThanMinPwdLengthException() {
        super(null, null, null, null);
    }

    public Collection<ErrorItem> getErrorFields() {
        return errors;
    }


}
