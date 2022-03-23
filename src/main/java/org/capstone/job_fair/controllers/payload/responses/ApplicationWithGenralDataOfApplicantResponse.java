package org.capstone.job_fair.controllers.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.dtos.attendant.CountryDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.*;
import org.capstone.job_fair.models.enums.ApplicationStatus;
import org.capstone.job_fair.models.enums.Gender;
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
    ApplicationStatus status;

    String candidateName;
    Integer candidateYearOfExp;
    JobLevel candidateJobLevel;
    String candidateJobTitle;
    List<CvSkillDTO> candidateSkills;
    List<CvActivityDTO> candidateActivities;
    List<CvCertificationDTO> candidateCertifications;
    List<CvEducationDTO> candidateEducation;
    List<CvReferenceDTO> candidateReferences;
    List<CvWorkHistoryDTO> candidateWorkHistories;
    Gender gender;
    String imageUrl;
    String country;

}
