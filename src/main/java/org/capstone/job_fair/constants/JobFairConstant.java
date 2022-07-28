package org.capstone.job_fair.constants;

public class JobFairConstant {
    private JobFairConstant() {

    }

    public static final String DEFAULT_SEARCH_OFFSET_VALUE = "0";
    public static final String DEFAULT_SEARCH_PAGE_SIZE_VALUE = "10";
    public static final String DEFAULT_SEARCH_SORT_BY_VALUE = "createTime";
    public static final String DEFAULT_SEARCH_SORT_DIRECTION = "ASC";
    public static final String DEFAULT_JOBFAIR_NAME = "";
    public static final String DEFAULT_CATEGORY_ID = "";
    public static final String DEFAULT_COUNTRY_ID = "";

    public enum AdminSearchStatus {
        TAKEN_PLACE, HAPPENING, COMING_SOON
    }
}
