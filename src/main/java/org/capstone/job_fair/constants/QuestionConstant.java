package org.capstone.job_fair.constants;

import org.capstone.job_fair.models.statuses.QuestionStatus;

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

    public static final long ONE_YEAR = 60 * 60 * 24 * 365 * 1000;


}
