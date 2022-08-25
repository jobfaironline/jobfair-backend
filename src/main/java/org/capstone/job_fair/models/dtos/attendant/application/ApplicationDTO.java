package org.capstone.job_fair.models.dtos.attendant.application;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.BoothJobPositionDTO;
import org.capstone.job_fair.models.enums.ApplicationStatus;
import org.capstone.job_fair.models.enums.JobLevel;
import org.capstone.job_fair.models.enums.TestStatus;
import org.capstone.job_fair.models.statuses.InterviewStatus;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ApplicationDTO {
    @EqualsAndHashCode.Include
    private String id;
    private String summary;
    private Long createDate;
    private ApplicationStatus status;
    @JsonBackReference
    private BoothJobPositionDTO boothJobPositionDTO;
    private String evaluateMessage;
    private Long evaluateDate;
    private String email;
    private String phone;
    private Integer yearOfExp;
    private String jobTitle;
    private JobLevel jobLevel;
    private List<ApplicationActivityDTO> activities;
    private List<ApplicationCertificationDTO> certifications;
    private List<ApplicationEducationDTO> educations;
    private List<ApplicationReferenceDTO> references;
    private List<ApplicationSkillDTO> skills;
    private List<ApplicationWorkHistoryDTO> workHistories;
    private AttendantDTO attendant;
    private String originCvId;
    private TestStatus testStatus;
    private String interviewName;
    private String interviewDescription;
    private Long beginTime;
    private Long endTime;
    private InterviewStatus interviewStatus;
    private String interviewUrl;
    private String waitingRoomId;
    private String interviewRoomId;
    private CompanyEmployeeDTO interviewer;
    private String attendantAdvantage;
    private String attendantDisadvantage;
    private String interviewNote;
    private Boolean isQualified;
    private Double matchingPoint;
    private String aboutMe;
    private String fullName;
    private String profileImageUrl;
    private String countryId;


}
