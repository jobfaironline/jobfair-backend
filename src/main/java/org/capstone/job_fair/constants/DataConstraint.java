package org.capstone.job_fair.constants;

import java.awt.image.BufferedImage;

public final class DataConstraint {
    private DataConstraint() {
    }

    public static final class Skill {
        private Skill() {
        }

        public static final int MIN_PROFICIENCY = 1;
        public static final int MAX_PROFICIENCY = 5;
        public static final int MIN_NAME_LENGTH = 1;
        public static final int MAX_NAME_LENGTH = 100;

    }

    public static class JobFair {
        private JobFair() {
        }

        public static final int MIN_TIME = 0;
        public static final int MAX_DESCRIPTION_LENGTH = 500000;
        public static final int MESSAGE_MIN_LENGTH = 1;
        public static final int MESSAGE_MAX_LENGTH = 500;
        public static final int COMPANY_REASON_CANCEL_JOB_FAIR_REGISTRATION_MAX_LENGTH = 1000;
        public static final int COMPANY_REASON_CANCEL_JOB_FAIR_REGISTRATION_MIN_LENGTH = 1;
        public static final int NAME_MAX_LENGTH = 100;
        public static final int MIN_ESTIMATE_PARTICIPANT_NUM = 0;
        public static final int MAX_ESTIMATE_PARTICIPANT_NUM = 1_000_000;
        public static final int TARGET_COMPANY_MAX_LENGTH = 500;
        public static final int TARGET_ATTENDANT_MAX_LENGTH = 500;
        public static final long MINIMUM_DECORATE_TIME = 60 * 60 * 1000L;
        public static final long MINIMUM_PUBLIC_TIME = 60 * 60 * 1000L;
        public static final long MINIMUM_BUFFER_TIME = 0L;
        public static final int HOST_NAME_MAX_LENGTH = 100;


    }

    public static final class CompanyRegistration {
        private CompanyRegistration() {
        }

        public static final int MESSAGE_MIN_LENGTH = 1;
        public static final int MESSAGE_MAX_LENGTH = 1000;
        public static final int OFFSET_MIN = 0;
        public static final int PAGE_SIZE_MIN = 0;
    }

    public static final class WorkHistory {
        private WorkHistory() {
        }

        public static final int POSITION_MIN_LENGTH = 1;
        public static final int POSITION_MAX_LENGTH = 100;
        public static final int COMPANY_MIN_LENGTH = 1;
        public static final int COMPANY_MAX_LENGTH = 100;
        public static final long FROM_DATE = -2208988800000L;
        public static final int MIN_DESCRIPTION_LENGTH = 1;
        public static final int MAX_DESCRIPTION_LENGTH = 5000;
    }

    public static final class Education {
        private Education() {
        }

        public static final int SUBJECT_MIN_LENGTH = 1;
        public static final int SUBJECT_MAX_LENGTH = 100;
        public static final int SCHOOL_MIN_LENGTH = 1;
        public static final int SCHOOL_MAX_LENGTH = 100;
        public static final long FROM_DATE = -2208988800000L;
        public static final int ACHIEVEMENT_MIN_LENGTH = 1;
        public static final int ACHIEVEMENT_MAX_LENGTH = 5000;
    }

    public static final class Certification {
        private Certification() {
        }

        public static final int NAME_MIN_LENGTH = 1;
        public static final int NAME_MAX_LENGTH = 1000;
        public static final int INSTITUTION_MIN_LENGTH = 1;
        public static final int INSTITUTION_MAX_LENGTH = 100;
        public static final int YEAR_MIN = 1900;
        public static final int URL_MIN_LENGTH = 1;
        public static final int URL_MAX_LENGTH = 2048;
    }

    public static final class Reference {
        private Reference() {
        }

        public static final int FULLNAME_MIN_LENGTH = 1;
        public static final int FULLNAME_MAX_LENGTH = 1000;
        public static final int POSITION_MIN_LENGTH = 1;
        public static final int POSITION_MAX_LENGTH = 100;
        public static final int COMPANY_MIN_LENGTH = 1;
        public static final int COMPANY_MAX_LENGTH = 100;
    }

    public static final class Activity {
        private Activity() {
        }

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
        private JobPosition() {
        }

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
        public static final int OFFSET_MIN = 0;
        public static final int PAGE_SIZE_MIN = 0;
    }

    public static final class Account {
        private Account() {
        }

        public static final int NAME_LENGTH = 100;
        public static final int URL_MIN_LENGTH = 1;
        public static final int URL_MAX_LENGTH = 2048;
        public static final int IMAGE_TYPE = BufferedImage.TYPE_INT_BGR;
        public static final String IMAGE_EXTENSION_TYPE = "jpg";
        public static final double WIDTH_FACTOR = 0.5;
        public static final double HEIGHT_FACTOR = 0.5;


    }

    public static final class Attendant {
        private Attendant() {
        }

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


    public static final class Company {
        private Company() {
        }

        public static final int NAME_MIN_LENGTH = 1;
        public static final int NAME_MAX_LENGTH = 1000;
        public static final int DESCRIPTION_MIN_LENGTH = 1;
        public static final int DESCRIPTION_MAX_LENGTH = 1000;
        public static final int ADDRESS_MIN_LENGTH = 1;
        public static final int ADDRESS_MAX_LENGTH = 1000;
        public static final int COMPANY_MIN_NUM = 1;
        public static final int CATEGORY_MIN = 1;
        public static final int CATEGORY_MAX = 4;
        public static final int DEFAULT_EMPLOYEE_MAX_NUM = 5;
        public static final int MIN_JOB_POSITION = 0;
        public static final int MAX_DESCRIPTION_LENGTH = 10000;
        public static final int MIN_DESCRIPTION_LENGTH = 1;
        public static final int TAX_ID_MIN_LENGTH = 10;
        public static final int TAX_ID_MAX_LENGTH = 13;
        public static final int COMPANY_LOGO_TYPE = BufferedImage.TYPE_INT_ARGB;
        public static final String COMPANY_LOGO_EXTENSION_TYPE = "png";
        public static final double WIDTH_FACTOR = 0.5;
        public static final double HEIGHT_FACTOR = 0.5;
    }


    public static final class DecoratedItem {
        private DecoratedItem() {
        }

        public static final int NAME_MAX_LENGTH = 100;
        public static final int NAME_MIN_LENGTH = 1;
        public static final int DESCRIPTION_MAX_LENGTH = 5000;
        public static final int DESCRIPTION_MIN_LENGTH = 1;
        public static final int SIZE_MIN = 0;
        public static final int SIZE_MAX = 100;

    }

    public static final class Layout {
        private Layout() {
        }

        public static final int NAME_MAX_LENGTH = 100;
        public static final int NAME_MIN_LENGTH = 1;
        public static final int DESCRIPTION_MAX_LENGTH = 500;
        public static final int DESCRIPTION_MIN_LENGTH = 1;
        public static final int IMAGE_TYPE = BufferedImage.TYPE_INT_BGR;
        public static final String IMAGE_EXTENSION_TYPE = "jpg";
        public static final double WIDTH_FACTOR = 0.5;
        public static final double HEIGHT_FACTOR = 0.5;
    }

    public static final class Application {
        private Application() {

        }

        public static final int OFFSET_MIN = 0;
        public static final int PAGE_SIZE_MIN = 0;

        public static final int SUMMARY_MAX_LENGTH = 3000;

        public static final int EVALUATE_MESSAGE_MAX_LENGTH = 200;
        public static final int EVALUATE_MESSAGE_MIN_LENGTH = 0;


    }

    public static final class Paging {
        private Paging() {

        }

        public static final int OFFSET_MIN = 0;
        public static final int PAGE_SIZE_MIN = 0;
    }

    public static final class Cv {
        private Cv() {
        }

        public static final int JOB_TITTLE_LENGTH = 100;
        public static final int YEAR_OF_EXPERIENCE_MIN = 0;
        public static final int YEAR_OF_EXPERIENCE_MAX = 70;
        public static final int MIN_NAME_LENGTH = 1;
        public static final int MAX_NAME_LENGTH = 100;
    }


}
