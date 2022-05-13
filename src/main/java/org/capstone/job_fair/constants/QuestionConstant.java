package org.capstone.job_fair.constants;

public class QuestionConstant {
    private QuestionConstant() {

    }

    public static final String DEFAULT_SEARCH_OFFSET_VALUE = "0";
    public static final String DEFAULT_SEARCH_PAGE_SIZE_VALUE = "10";
    public static final String DEFAULT_SEARCH_SORT_BY_VALUE = "createDate";
    public static final String DEFAULT_SEARCH_SORT_DIRECTION = "ASC";
    public static final String DEFAULT_SEARCH_QUESTION_CONTENT = "";
    public static final String DEFAULT_FROM_DATE = "0";
    public static final String DEFAULT_TO_DATE = "0";
    public static final String DEFAULT_QUESTION_STATUS = "ACTIVE";

    public static final long ONE_YEAR = 60 * 60 * 24 * 365 * 1000L;


    public static final class XLSXFormat {
        public static final int COLUMN_NUM = 4;
        public static final int CONTENT_INDEX = 0;
        public static final int IS_QUESTION_INDEX = 1;
        public static final int IS_CORRECT_INDEX = 2;
        public static final int ERROR_INDEX = 3;
    }
}
