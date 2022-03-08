package org.capstone.job_fair.controllers.payload.requests.company;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.models.enums.JobLevel;
import org.capstone.job_fair.models.enums.JobType;
import org.capstone.job_fair.models.enums.Language;
import org.capstone.job_fair.utils.OpenCSV.ListConverter;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class CreateJobPositionRequest {
    @Size(max = DataConstraint.JobPosition.TITLE_LENGTH)
    @NotEmpty
    @XSSConstraint
    @CsvBindByName(column = "title")
    private String title;
    @NotEmpty
    @XSSConstraint
    @Size(max = DataConstraint.JobPosition.CONTACT_PERSON_NAME_LENGTH)
    @CsvBindByName(column = "contactPersonName")
    private String contactPersonName;
    @EmailConstraint
    @NotEmpty
    @CsvBindByName(column = "contactEmail")
    private String contactEmail;
    @NotNull
    @CsvBindByName(column = "preferredLanguage")
    private Language preferredLanguage;
    @NotNull
    @CsvBindByName(column = "level")
    private JobLevel level;
    @NotNull
    @CsvBindByName(column = "jobType")
    private JobType jobType;
    @NotNull
    @CsvBindByName(column = "locationId")
    private String locationId;
    @NotNull
    @CsvBindByName(column = "companyId")
    private String companyId;
    @NotNull
    @Valid
    @Size(min = DataConstraint.JobPosition.CATEGORY_MIN, max = DataConstraint.JobPosition.CATEGORY_MAX)
    @CsvCustomBindByName(column = "subCategoryIds", converter = ListConverter.class)
    private List<Integer> subCategoryIds;
    @NotNull
    @Valid
    @Size(min = DataConstraint.JobPosition.SKILL_TAG_MIN, max = DataConstraint.JobPosition.SKILL_TAG_MAX)
    @CsvCustomBindByName(column = "skillTagIds", converter = ListConverter.class)
    private List<Integer> skillTagIds;

    @XSSConstraint
    @NotNull
    @Size(min = DataConstraint.Company.MIN_DESCRIPTION_LENGTH, max = DataConstraint.Company.MAX_DESCRIPTION_LENGTH)
    @CsvBindByName(column = "description")
    private String description;

    @XSSConstraint
    @NotNull
    @Size(min = DataConstraint.Company.MIN_DESCRIPTION_LENGTH, max = DataConstraint.Company.MAX_DESCRIPTION_LENGTH)
    @CsvBindByName(column = "requirements")
    private String requirements;
}
