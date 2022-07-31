package org.capstone.job_fair.constants;

final public class AssignmentConstant {
    private AssignmentConstant() {
    }

    public static final String DEFAULT_SEARCH_OFFSET_VALUE = "0";
    public static final String DEFAULT_SEARCH_PAGE_SIZE_VALUE = "10";
    public static final String DEFAULT_SEARCH_SORT_BY_VALUE = "createTime";
    public static final String DEFAULT_SEARCH_SORT_DIRECTION = "ASC";
    public static final String DEFAULT_SEARCH_JOB_FAIR_NAME = "";

    public static final class XLSXFormat {
        private XLSXFormat() {
        }

        public static final int COLUMN_NUM = 4;
        public static final int EMPLOYEE_ID_INDEX = 0;
        public static final int SLOT_NAME_INDEX = 1;
        public static final int BOOTH_NAME_INDEX = 2;
        public static final int ASSIGMENT_INDEX = 3;
        public static final int ERROR_INDEX = 4;
    }

    public static final class XLSXSupervisorFormat{
        private XLSXSupervisorFormat(){}
        public static final int COLUMN_NUM = 4;
        public static final int EMPLOYEE_ID_INDEX = 0;
        public static final int JOB_FAIR_DAY_INDEX = 1;
        public static final int SHIFT_INDEX = 2;
        public static final int ASSIGNMENT_TYPE_INDEX = 3;
        public static final int ERROR_INDEX = 4;
    }
}
