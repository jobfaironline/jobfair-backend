package org.capstone.job_fair.constants;

public final class SkillExtractorApiEndpoint {
    private SkillExtractorApiEndpoint() {
    }

    public static final String EXTRACT_KEYWORD = "/extract-keyword";
    public static final String MATCHING_POINT = "/matching-point";
    public static final String MATCHING_POINT_APPLICATION = MATCHING_POINT + "/application";
    public static final String MATCHING_POINT_JOB_POSITION = MATCHING_POINT + "/job-position";
}
