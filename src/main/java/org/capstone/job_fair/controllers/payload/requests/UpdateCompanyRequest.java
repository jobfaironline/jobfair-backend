package org.capstone.job_fair.controllers.payload.requests;

import lombok.*;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
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

    @Size(max = DataConstraint.Company.NAME_LENGTH)
    @NotEmpty
    @XSSConstraint
    private String name;

    @Size(max = DataConstraint.Company.ADDRESS_LENGTH)
    @NotEmpty
    @XSSConstraint
    private String address;

    @NotEmpty
    @PhoneConstraint
    private String phone;

    @NotEmpty
    @EmailConstraint
    private String email;

    @Min(value = DataConstraint.Company.COMPANY_MIN_NUM)
    private Integer employeeMaxNum;

    @NotNull
    private String taxId;

    @NotEmpty
    @XSSConstraint
    private String url;

    private Integer sizeId;

    private Integer status;

    @NotNull
    @Valid
    private List<String> mediaUrls;

    @NotNull
    @Valid
    @Size(min = DataConstraint.JobPosition.CATEGORY_MIN, max = DataConstraint.JobPosition.CATEGORY_MAX)
    private List<Integer> benefitIds;

    @NotNull
    @Valid
    @Size(min = DataConstraint.Company.CATEGORY_MIN, max = DataConstraint.Company.CATEGORY_MAX)
    private List<Integer> subCategoriesIds;
}
