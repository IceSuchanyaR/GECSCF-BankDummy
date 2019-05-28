package gec.scf.dummy.sponsor.ptt.document.status.domain;

public class UpdateDocumentStatuses {
    public static interface Validation {

        public interface Create {
        }

        public interface Update {
        }

        public interface Delete {
        }
    }

    public static interface View {
        public interface ListForAll {
        }

        public interface DetailForAll {
        }

    }

    public static interface Event {
        public static final String CREATE_UPDATE_DOCUMENT_STATUS_COMPLETED = "CREATE_UPDATE_DOCUMENT_STATUS_COMPLETED";
        public static final String UPDATE_UPDATE_DOCUMENT_STATUS_COMPLETED = "UPDATE_UPDATE_DOCUMENT_STATUS_COMPLETED";
        public static final String DELETE_UPDATE_DOCUMENT_STATUS_COMPLETED = "DELETE_UPDATE_DOCUMENT_STATUS_COMPLETED";
    }
}
