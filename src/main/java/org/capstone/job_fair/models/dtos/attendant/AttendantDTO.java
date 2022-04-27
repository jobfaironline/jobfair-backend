package org.capstone.job_fair.models.dtos.attendant;

import lombok.*;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.profile.*;
import org.capstone.job_fair.models.enums.JobLevel;
import org.capstone.job_fair.models.enums.Marital;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AttendantDTO implements Serializable {
    @EqualsAndHashCode.Include
    private AccountDTO account;
    private String title;
    private String address;
    private Long dob;
    private String jobTitle;
    private Double yearOfExp;
    private Marital maritalStatus;
    private Integer countryId;
    private Integer residenceId;
    private JobLevel jobLevel;

    private List<SkillDTO> skills;
    private List<WorkHistoryDTO> workHistories;
    private List<EducationDTO> educations;
    private List<CertificationDTO> certifications;
    private List<ReferenceDTO> references;
    private List<ActivityDTO> activities;
}
