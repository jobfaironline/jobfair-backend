package org.capstone.job_fair.models.dtos.attendant.cv;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.account.GenderDTO;
import org.capstone.job_fair.models.dtos.attendant.CountryDTO;
import org.capstone.job_fair.models.dtos.attendant.JobLevelDTO;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.models.statuses.CvStatus;
import org.capstone.job_fair.models.statuses.MaritalStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CvDTO {
    private String firstname;
    private String lastname;
    private String middlename;
    private Double yearOfExp;
    private String email;
    private String phone;
    private Long dob;
    private MaritalStatus maritalStatus;
    private CvStatus cvStatus;
    private String address;
    private String summary;
    private Long createDate;
    //Many to one
    private NationalityDTO nationality;
    private CountryDTO country;
    private JobLevelDTO currentJob;
    private Gender gender;
    private AccountDTO account;
    //one to many
    private List<SkillDTO> skills;
    private List<WorkHistoryDTO> workHistories;
    private List<EducationDTO> educations;
    private List<CertificationDTO> certificates;
    private List<ReferenceDTO> references;
    private List<ActivityDTO> activities;
    //

}
