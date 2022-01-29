package org.capstone.job_fair.constants;

import java.util.Calendar;

public class DataConstraint {
    public static final class Skill {
        public static final int MIN_PROFICIENCY = 1;
        public static final int MAX_PROFICIENCY = 5;
        public static final int MIN_NAME_LENGTH = 1;
        public static final int MAX_NAME_LENGTH = 100;

    }

    public static class JobFair{
        public static final int MIN_TIME = 0;
        public static final int MAX_DESCRIPTION_LENGTH = 500000;
        public  static final long VALID_TIME = 60*60*24*365L;
        public  static final long VALID_REGISTER_TIME = 60*60*24*365*1000L;
        public  static final long VALID_BUY_BOOTH_TIME = +0*60*24*365*1000L;
        public  static final long VALID_EVENT_TIME = 60*60*24*365*1000L;
        public  static final long VALID_REGISTER_TO_BUY_BOOTH_TIME = 0;
        public  static final long VALID_BUY_BOOTH_TO_PUBLIC_TIME = 0;
        public  static final long VALID_PUBLIC_TO_EVENT_TIME = 0;


    }

    public static final class WorkHistory {
        public static final int POSITION_MIN_LENGTH = 1;
        public static final int POSITION_MAX_LENGTH = 100;
        public static final int COMPANY_MIN_LENGTH = 1;
        public static final int COMPANY_MAX_LENGTH = 100;
        public static final long FROM_DATE = -2208988800000L;
        public static final int MIN_DESCRIPTION_LENGTH = 1;
        public static final int MAX_DESCRIPTION_LENGTH = 5000;
    }

    public static final class Education {
        public static final int SUBJECT_MIN_LENGTH = 1;
        public static final int SUBJECT_MAX_LENGTH = 100;
        public static final int SCHOOL_MIN_LENGTH = 1;
        public static final int SCHOOL_MAX_LENGTH = 100;
        public static final long FROM_DATE = -2208988800000L;
        public static final int ACHIEVEMENT_MIN_LENGTH = 1;
        public static final int ACHIEVEMENT_MAX_LENGTH = 5000;
    }

    public static final class Certification {
        public static final int NAME_MIN_LENGTH = 1;
        public static final int NAME_MAX_LENGTH = 1000;
        public static final int INSTITUTION_MIN_LENGTH = 1;
        public static final int INSTITUTION_MAX_LENGTH = 100;
        public static final int YEAR_MIN = 1900;
        public static final int URL_MIN_LENGTH = 1;
        public static final int URL_MAX_LENGTH = 2048;
    }

    public static final class Reference {
        public static final int FULLNAME_MIN_LENGTH = 1;
        public static final int FULLNAME_MAX_LENGTH = 1000;
        public static final int POSITION_MIN_LENGTH = 1;
        public static final int POSITION_MAX_LENGTH = 100;
        public static final int COMPANY_MIN_LENGTH = 1;
        public static final int COMPANY_MAX_LENGTH = 100;
    }

    public static final class Activity {
        public static final int MIN_NAME_LENGTH = 1;
        public static final int MAX_NAME_LENGTH = 100;
        public static final int FUNCTION_MIN_LENGTH = 1;
        public static final int FUNCTION_MAX_LENGTH = 100;
        public static final int ORGANIZATION_MIN_LENGTH = 1;
        public static final int ORGANIZATION_MAX_LENGTH = 100;
        public static final long FROM_DATE = -2208988800000L;
        public static final int MIN_DESCRIPTION_LENGTH = 1;
        public static final int MAX_DESCRIPTION_LENGTH = 5000;

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
        public static final int URL_MIN_LENGTH = 1;
        public static final int URL_MAX_LENGTH = 2048;

    }

    public static final class Attendant {
        public static final int ADDRESS_LENGTH = 1000;
        public static final int TITTLE_LENGTH = 100;
        public static final int JOB_TITTLE_LENGTH = 100;
        public static final int YEAR_OF_EXPERIENCE_MIN = 0;
        public static final int YEAR_OF_EXPERIENCE_MAX = 70;
        public static final long MIN_DOB = -2208988800000L;
        public static final long MAX_DOB = 1420071120000L;
        public static final int TITLE_MIN_LENGTH = 1;
        public static final int TITLE_MAX_LENGTH = 100;
        public static final int ADDRESS_MIN_LENGTH = 1;
        public static final int ADDRESS_MAX_LENGTH = 1000;
        public static final int JOB_TITLE_MIN_LENGTH = 1;
        public static final int JOB_TITLE_MAX_LENGTH = 100;
    }


    public static final class Residence {

    }
    public static final class Company {
        public static final int NAME_MIN_LENGTH = 1;
        public static final int NAME_MAX_LENGTH = 1000;
        public static final int ADDRESS_MIN_LENGTH = 1;
        public static final int ADDRESS_MAX_LENGTH = 1000;
        public static final int COMPANY_MIN_NUM = 1;
        public static final int CATEGORY_MIN= 1;
        public static final int CATEGORY_MAX= 4;
        public static final int DEFAULT_EMPLOYEE_MAX_NUM = 5;
    }

}
