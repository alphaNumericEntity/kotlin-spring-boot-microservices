package com.vnapnic.common.beans

enum class ErrorCode(val code: Long, val message: String) {

    SERVER_UNKNOWN_ERROR(1000, "Server unknown error."),

    EMAIL_PASSWORD_NOT_CORRECT(1001, "Email/Password is not correct."),
    SOCIAL_PASSWORD_NOT_CORRECT(1002, "Social/Password is not correct."),
    PHONE_NUMBER_PASSWORD_NOT_CORRECT(1003, "Phone number/Password is not correct."),

    PASSWORD_IS_NULL_BLANK(1004, "Password is null or blank."),
    EMAIL_IS_NULL_BLANK(1005, "Email is null or blank."),

    EMAIL_IS_EXISTS(1006, "Email is exists."),
    SOCIAL_IS_EXISTS(1007, "Social is exists."),

    PHONE_NUMBER_IS_NULL_BLANK(1008, "Phone number is null or blank."),
    PHONE_NUMBER_IS_EXISTS(1009, "Phone number is exists."),

    CODE_NOT_CORRECT(1010, "CODE is not correct."),

    FILE_TOO_LARGE(1011, "File too large."),

    FILE_UPLOAD_FAIL(1012, "Upload file fail."),

    UNSUPPORTED_MEDIA_TYPE(1013, "Unsupported Media Type."),

    UNSUPPORTED_DEVICE(1014, "Unsupported device."),

    PHONE_NUMBER_WRONG_FORMAT(1015, "Wrong phone number format."),

    EMAIL_WRONG_FORMAT(1016, "Wrong email format."),

    USER_NOT_FOUND(1017, "User not found."),

    WARNING_DATA_FORMAT(1018, "Warning data format.")
}