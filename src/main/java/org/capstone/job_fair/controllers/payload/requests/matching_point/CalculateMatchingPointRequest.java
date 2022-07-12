package org.capstone.job_fair.controllers.payload.requests.matching_point;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Data
public class CalculateMatchingPointRequest {
    private List<String> requirementKeyWords;
    private List<String> descriptionKeyWords;
    private List<String> jobSkills;
    private List<String> otherRequireKeywords;

    private List<String> attendantSkills;
    private List<String> attendantEducationKeyWords;
    private List<String> attendantWorkHistoryKeyWords;
    private List<String> attendantCertificationKeyWords;
    private List<String> attendantActivityKeyWords;
}


