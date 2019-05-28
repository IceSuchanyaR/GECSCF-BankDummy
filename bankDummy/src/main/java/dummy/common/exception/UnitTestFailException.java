package gec.scf.dummy.common.exception;

import java.io.Serializable;

public class UnitTestFailException extends GECException {

    public UnitTestFailException(String module, String event, Serializable referenceObject, Throwable cause) {
        super(module, event, referenceObject, cause);
    }

    public UnitTestFailException(String message) {
        super(message);
    }

    public UnitTestFailException() {
        super(null);
    }
}
