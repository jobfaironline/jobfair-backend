package org.capstone.job_fair.models.dtos.company.job;

import lombok.*;
import org.capstone.job_fair.models.dtos.company.CompanyRegistrationDTO;
import org.capstone.job_fair.models.dtos.company.SkillTagDTO;
import org.capstone.job_fair.models.dtos.company.SubCategoryDTO;
import org.capstone.job_fair.models.entities.attendant.JobLevelEntity;
import org.capstone.job_fair.models.entities.attendant.LanguageEntity;
import org.capstone.job_fair.models.entities.company.CompanyRegistrationEntity;
import org.capstone.job_fair.models.entities.company.SkillTagEntity;
import org.capstone.job_fair.models.entities.company.SubCategoryEntity;
import org.capstone.job_fair.models.entities.company.job.JobTypeEntity;
import org.capstone.job_fair.models.enums.JobLevel;
import org.capstone.job_fair.models.enums.JobType;
import org.capstone.job_fair.models.enums.Language;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationJobPositionDTO implements Serializable {

    private String id;
    private String description;
    private String requirements;
    private double minSalary;
    private double maxSalary;
    private int numOfPosition;

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
}
