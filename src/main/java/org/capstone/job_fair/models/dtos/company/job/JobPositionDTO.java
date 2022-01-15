package org.capstone.job_fair.models.dtos.company.job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.dtos.company.SkillTagDTO;
import org.capstone.job_fair.models.dtos.company.SubCategoryDTO;
import org.capstone.job_fair.models.enums.JobLevel;
import org.capstone.job_fair.models.enums.JobType;
import org.capstone.job_fair.models.enums.Language;

import java.io.Serializable;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPositionDTO implements Serializable {
    private String id;
    private String title;
    private String description;
    private String requirements;
    private Double minSalary;
    private Double maxSalary;
    private String contactPersonName;
    private String contactEmail;
    private String companyName;
    private Language language;
    private JobLevel level;
    private JobType jobType;
    private String locationId;
    private String comapnyId;
    private List<SubCategoryDTO> subCategoryDTOs;
    private List<SkillTagDTO> skillTagDTOS;
}
