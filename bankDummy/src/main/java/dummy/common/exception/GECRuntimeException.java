package gec.scf.dummy.common.exception;

import java.io.Serializable;

public class GECRuntimeException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -5486320443494575546L;
    private String module;

    private String event;

    private Serializable referenceObject;

    public GECRuntimeException(final String module, final String event,
                               final Serializable referenceObject, Throwable cause) {
        super("", cause);
        this.module = module;
        this.referenceObject = referenceObject;
        this.event = event;
    }

    public GECRuntimeException(String message) {
        super(message);
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Object getReferenceObject() {
        return referenceObject;
    }

    public void setReferenceObject(Serializable referenceObject) {
        this.referenceObject = referenceObject;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
