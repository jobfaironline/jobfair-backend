package org.capstone.job_fair.controllers.payload.requests.company;

import lombok.*;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.models.statuses.CompanyStatus;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PhoneConstraint;
import org.capstone.job_fair.validators.XSSConstraint;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Min;
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

    @Length(min = DataConstraint.Company.TAX_ID_LENGTH, max = DataConstraint.Company.TAX_ID_LENGTH)
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

    @XSSConstraint
    private String companyDescription;

    private Integer sizeId;

    private CompanyStatus status;

    @Valid
    private List<String> mediaUrls;

    @Valid
    private List<BenefitRequest> benefits;

    @Valid
    @Size(min = DataConstraint.Company.CATEGORY_MIN, max = DataConstraint.Company.CATEGORY_MAX)
    private List<Integer> subCategoriesIds;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BenefitRequest {
        private Integer id;
        private String description;
    }
}
