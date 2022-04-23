package org.capstone.job_fair.models.dtos.company.job;

import lombok.*;
import org.capstone.job_fair.models.dtos.company.JobFairBoothDTO;
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
public class BoothJobPositionDTO implements Serializable {
    @EqualsAndHashCode.Include
    private String id;
    private String title;
    private String contactPersonName;
    private String contactEmail;
    private Language language;
    private JobLevel jobLevel;
    private JobType jobType;
    private List<SubCategoryDTO> subCategoryDTOs;
    private List<SkillTagDTO> skillTagDTOS;
    private String description;
    private String requirements;
    private Double minSalary;
    private Double maxSalary;
    private Integer numOfPosition;
    private String locationId;
    private String originJobPosition;
    private String note;
    private Boolean isHaveTest;
    private Integer testTimeLength;
    private Integer numOfQuestion;
    private Double passMark;
    private JobFairBoothDTO jobFairBooth;
}
