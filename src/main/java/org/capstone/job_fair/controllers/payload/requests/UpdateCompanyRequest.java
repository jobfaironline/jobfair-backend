package org.capstone.job_fair.controllers.payload.requests;

import lombok.*;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.models.statuses.CompanyStatus;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PhoneConstraint;
import org.capstone.job_fair.validators.XSSConstraint;


import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateCompanyRequest {
    @NotNull
    private String id;

    private String taxId;

    @Size(min = DataConstraint.Company.NAME_MIN_LENGTH, max = DataConstraint.Company.NAME_MAX_LENGTH)
    @XSSConstraint
    private String name;

    @Size(min = DataConstraint.Company.ADDRESS_MIN_LENGTH, max = DataConstraint.Company.ADDRESS_MAX_LENGTH)
    @XSSConstraint
    private String address;

    @PhoneConstraint
    private String phone;

    @EmailConstraint
    private String email;

    @Min(value = DataConstraint.Company.COMPANY_MIN_NUM)
    private Integer employeeMaxNum;

    @XSSConstraint
    private String url;

    private Integer sizeId;

    private CompanyStatus status;

    @Valid
    private List<String> mediaUrls;

    @Valid
    @Size(min = DataConstraint.JobPosition.CATEGORY_MIN, max = DataConstraint.JobPosition.CATEGORY_MAX)
    private List<Integer> benefitIds;

    @Valid
    @Size(min = DataConstraint.Company.CATEGORY_MIN, max = DataConstraint.Company.CATEGORY_MAX)
    private List<Integer> subCategoriesIds;
}
