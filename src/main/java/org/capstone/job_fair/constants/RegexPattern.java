package org.capstone.job_fair.constants;

import java.util.regex.Pattern;

public class RegexPattern {
    public static final Pattern VALID_EMAIL_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("^.{2,}$");
    public static final Pattern VALID_PHONE_REGEX =
            Pattern.compile("^[0-9]{10,11}$");
}
