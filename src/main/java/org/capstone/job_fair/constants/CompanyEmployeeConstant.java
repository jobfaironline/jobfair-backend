package org.capstone.job_fair.constants;

public final class CompanyEmployeeConstant {
    private CompanyEmployeeConstant() {
    }

    public static final String DEFAULT_SEARCH_OFFSET_VALUE = "0";
    public static final String DEFAULT_SEARCH_PAGE_SIZE_VALUE = "10";
    public static final String DEFAULT_SEARCH_SORT_BY_VALUE = "account.createTime";
    public static final String DEFAULT_SEARCH_SORT_DIRECTION = "DESC";
    public static final String DEFAULT_SEARCH_CONTENT = "";

    public static final class XLSXFormat {
        private XLSXFormat() {
        }

        public static final int COLUMN_NUM = 6;
        public static final int FIRST_NAME_INDEX = 0;
        public static final int MIDDLE_NAME_INDEX = 1;
        public static final int LAST_NAME_INDEX = 2;
        public static final int DEPARTMENT_INDEX = 3;
        public static final int EMPLOYEE_ID_INDEX = 4;
        public static final int EMAIL_INDEX = 5;
        public static final int ERROR_INDEX = 6;
    }
}
