package org.capstone.job_fair.constants;

public class JobPositionConstant {
    private JobPositionConstant() {

    }

    public static final String DEFAULT_SEARCH_OFFSET_VALUE = "0";
    public static final String DEFAULT_SEARCH_PAGE_SIZE_VALUE = "10";
    public static final String DEFAULT_SEARCH_SORT_BY_VALUE = "createdDate";
    public static final String DEFAULT_SEARCH_SORT_DIRECTION = "ASC";

    public static final class XLSXFormat {
        private XLSXFormat(){}
        public static final int COLUMN_NUM = 11;
        public static final int TITLE_INDEX = 0;
        public static final int CONTACT_PERSON_NAME_INDEX = 1;
        public static final int CONTACT_EMAIL_INDEX = 2;
        public static final int PREFER_LANGUAGE_INDEX = 3;
        public static final int LEVEL_INDEX = 4;
        public static final int JOB_TYPE_INDEX = 5;
        public static final int LOCATION_INDEX = 6;
        public static final int SUB_CATEGORIES_INDEX = 7;
        public static final int SKILL_TAG_IDS_INDEX = 8;
        public static final int DESCRIPTION = 9;
        public static final int REQUIREMENTS = 10;
        public static final int ERROR_INDEX = 11;

    }
}
