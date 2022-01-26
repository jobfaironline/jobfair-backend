package org.capstone.job_fair.controllers.payload.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.models.enums.JobLevel;
import org.capstone.job_fair.models.enums.JobType;
import org.capstone.job_fair.models.enums.Language;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class CreateJobPositionRequest {
    @Size(max = DataConstraint.JobPosition.TITLE_LENGTH)
    @NotEmpty
    @XSSConstraint
    private String title;
    @NotEmpty
    @XSSConstraint
    @Size(max = DataConstraint.JobPosition.CONTACT_PERSON_NAME_LENGTH)
    private String contactPersonName;
    @EmailConstraint
    @NotEmpty
    private String contactEmail;
    @NotNull
    private Language preferredLanguage;
    @NotNull
    private JobLevel level;
    @NotNull
    private JobType jobType;
    @NotNull
    private String locationId;
    @NotNull
    private String companyId;
    @NotNull
    @Valid
    @Size(min = DataConstraint.JobPosition.CATEGORY_MIN, max = DataConstraint.JobPosition.CATEGORY_MAX)
    private List<Integer> subCategoryIds;
    @NotNull
    @Valid
    @Size(min = DataConstraint.JobPosition.SKILL_TAG_MIN, max = DataConstraint.JobPosition.SKILL_TAG_MAX)
    private List<Integer> skillTagIds;
}
