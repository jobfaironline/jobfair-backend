package org.capstone.job_fair.controllers.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.dtos.attendant.cv.CvSkillDTO;
import org.capstone.job_fair.models.enums.JobLevel;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationWithGenralDataOfApplicantResponse {
    String id;

    Long appliedDate;
    String jobPositionTitle;
    String jobFairName;
    String jobFairId;
    String jobPositionId;
    String applicationSummary;

    String candidateName;
    Integer candidateYearOfExp;
    JobLevel candidateJobLevel;
    String candidateJobTitle;
    List<CvSkillDTO> candidateSkills;
}
