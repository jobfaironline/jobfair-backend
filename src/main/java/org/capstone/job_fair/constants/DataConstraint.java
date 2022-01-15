package org.capstone.job_fair.constants;

public class DataConstraint {
    public static final class JobPosition{
        public static final int TITLE_LENGTH = 200;
        public static final int DESCRIPTION_LENGTH = 1000;
        public static final int REQUIREMENT_LENGTH = 1000;
        public static final long SALARY_MAX = 10000;
        public static final long SALARY_MIN = 0;
        public static final int CONTACT_PERSON_NAME_LENGTH = 100;
        public static final int CONTACT_EMAIL_LENGTH = 322;
        public static final int CATEGORY_MIN = 1;
        public static final int CATEGORY_MAX = 3;
        public static final int SKILL_TAG_MAX = 5;
        public static final int SKILL_TAG_MIN = 1;
    }
}
