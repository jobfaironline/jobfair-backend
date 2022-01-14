package org.capstone.job_fair.controllers.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.models.enums.Language;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@ToString
public class CreateJobPositionRequest {
    @NotNull
    @Size(max = DataConstraint.JobPosition.TITLE_LENGTH)
    @XSSConstraint
    private String title;
    @NotNull
    @Size(max = DataConstraint.JobPosition.DESCRIPTION_LENGTH)
    @XSSConstraint
    private String description;
    @NotNull
    @XSSConstraint
    @Size(max= DataConstraint.JobPosition.REQUIREMENT_LENGTH)
    private String requirements;
    @NotNull
    @Min(value = DataConstraint.JobPosition.SALARY_MIN)
    @Max(value = DataConstraint.JobPosition.SALARY_MAX)
    private Double minSalary;
    @NotNull
    @Min(value = DataConstraint.JobPosition.SALARY_MIN)
    @Max(value = DataConstraint.JobPosition.SALARY_MAX)
    private Double maxSalary;
    @Size(max = DataConstraint.JobPosition.CONTACT_PERSON_NAME_LENGTH)
    @NotNull
    @XSSConstraint
    private String contactPerson;
    @Size(max = DataConstraint.JobPosition.CONTACT_EMAIL_LENGTH)
    @EmailConstraint
    @NotNull
    private String contactEmail;
    @NotNull
    private Language preferredLanguage;
    @NotNull
    private String levelId;
    @NotNull
    private String jobTypeId;
    @NotNull
    private String locationId;
    @NotNull
    private String companyId;
}
