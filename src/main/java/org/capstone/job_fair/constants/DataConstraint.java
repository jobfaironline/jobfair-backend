package org.capstone.job_fair.constants;

public class DataConstraint {
    public static final class Skill {
        public static final int MIN_PROFICIENCY = 1;
        public static final int MAX_PROFICIENCY = 5;
    }

    public static final class JobPosition {
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
        public static final int EMPLOYEE_MIN = 1;
        public static final int EMPLOYEE_MAX = 100000;
    }

    public static final class Account {
        public static final int NAME_LENGTH = 100;
    }

    public static final class Attendant {
        public static final int ADDRESS_LENGTH = 1000;
        public static final int TITTLE_LENGTH = 100;
        public static final int JOB_TITTLE_LENGTH = 100;
        public static final int YEAR_OF_EXPERIENCE_MIN = 0;
        public static final long MIN_DOB = -2208988800000L;
        public static final long MAX_DOB = 1420071120000L;

    }


    public static final class Residence {

    }
    public static final class Company {
        public static final int NAME_LENGTH = 1000;
        public static final int ADDRESS_LENGTH = 1000;
        public static final int COMPANY_MIN_NUM = 1;
        public static final int CATEGORY_MIN= 1;
        public static final int CATEGORY_MAX= 4;
        public static final int DEFAULT_EMPLOYEE_MAX_NUM = 5;

    }
}
