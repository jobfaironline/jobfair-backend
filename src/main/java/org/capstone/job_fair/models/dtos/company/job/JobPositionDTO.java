package org.capstone.job_fair.models.dtos.company.job;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.dtos.company.misc.SkillTagDTO;
import org.capstone.job_fair.models.dtos.company.misc.SubCategoryDTO;
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
public class JobPositionDTO implements Serializable {
    @EqualsAndHashCode.Include
    private String id;
    private String title;
    private String contactPersonName;
    private String contactEmail;
    private Language language;
    private JobLevel level;
    private JobType jobType;
    @JsonBackReference
    private CompanyDTO companyDTO;
    private List<SubCategoryDTO> subCategoryDTOs;
    private List<SkillTagDTO> skillTagDTOS;
    private String description;
    private String requirements;
    private Long createdDate;
    private Long updateDate;
    private String descriptionKeyWord;
    private String requirementKeyWord;

}
