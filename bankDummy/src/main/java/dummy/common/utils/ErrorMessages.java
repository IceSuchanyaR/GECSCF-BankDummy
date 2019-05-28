package gec.scf.dummy.common.utils;

public class ErrorMessages {

	private ErrorMessages() {
		throw new IllegalStateException("Utility class");
	}

	public static final String FIELD_IS_REQUIRED = "errors.required";
	public static final String LENGTH_NOT_OVER = "errors.maxlength";
	public static final String DUPLICATE = "errors.dupplicate";
	public static final String HOLIDAY_SHOULD_NOT_IN_WEEKEND = "errors.holiday_should_not_in_weekend";
	public static final String EXPIRY_DATE_LESS_THAN_TODAY = "errors.expire_date_must_be_greater_than_today";
	public static final String EXPIRY_DATE_LESS_THAN_ACTIVE_DATE = "errors.expire_date_must_be_greater_than_active_date";
	public static final String MAX_PWD_LENGTH_LESS_THAN_MIN_PWD_LENGTH = "errors.max_pwd_must_be_greater_than_min_pwd";
}
