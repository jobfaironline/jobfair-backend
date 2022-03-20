package org.capstone.job_fair.models.dtos.company.job;

import lombok.*;
import org.capstone.job_fair.models.dtos.company.CompanyRegistrationDTO;
import org.capstone.job_fair.models.dtos.company.SkillTagDTO;
import org.capstone.job_fair.models.dtos.company.SubCategoryDTO;
import org.capstone.job_fair.models.enums.JobLevel;
import org.capstone.job_fair.models.enums.JobType;
import org.capstone.job_fair.models.enums.Language;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RegistrationJobPositionDTO implements Serializable {
    @EqualsAndHashCode.Include
    private String id;
    private String description;
    private String requirements;
    private Double minSalary;
    private Double maxSalary;
    private Integer numOfPosition;

    private String title;
    private String contactPersonName;
    private String contactEmail;
    private Language language;
    private JobLevel jobLevel;
    private JobType jobType;
    private CompanyRegistrationDTO companyRegistration;
    private List<SubCategoryDTO> subCategoryDTOs;
    private List<SkillTagDTO> skillTagDTOS;
    private String locationId;
    private String originJobPosition;
}
