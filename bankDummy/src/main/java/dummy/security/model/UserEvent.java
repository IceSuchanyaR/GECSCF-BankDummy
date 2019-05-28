package dummy.security.model;

public interface UserEvent {

    final String MODULE_USER = "MODULE_USER";
    final String UPDATE_USER_FAIL_USER_NOT_FOUND = "UPDATE_USER_FAIL_USER_NOT_FOUND";
    final String UPDATE_USER_FAIL_VERSION_MISMATCH = "UPDATE_USER_FAIL_VERSION_MISMATCH";
    final String DELETE_USER_FAIL_USER_NOT_FOUND = "DELETE_USER_FAIL_USER_NOT_FOUND";
    final String DELETE_USER_FAIL_VERSION_MISMATCH = "DELETE_USER_FAIL_VERSION_MISMATCH";

    public static interface Validation {
        public interface Create {

        }

        public interface Update {

        }

        public interface Delete {

        }
    }

    public static interface View {

        public interface DetailForAllUser {

        }

        public interface DetailForCommonUser {

        }
    }
}
