package org.capstone.job_fair.controllers.payload.requests;

import lombok.*;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PhoneConstraint;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CreateCompanyRequest {
    @NotBlank
    @NotBlank(message = MessageConstant.InvalidFormatValidationMessage.NOT_BLANK_FORMAT)
    private String taxID;

    @NotNull
    @NotBlank(message = MessageConstant.InvalidFormatValidationMessage.NOT_BLANK_FORMAT)
    @Size(max = DataConstraint.Company.NAME_LENGTH)
    private String name;

    @NotNull
    @NotBlank(message = MessageConstant.InvalidFormatValidationMessage.NOT_BLANK_FORMAT)
    @Size(max = DataConstraint.Company.ADDRESS_LENGTH)
    @XSSConstraint
    private String address;

    @NotNull
    @PhoneConstraint
    @XSSConstraint
    private String phone;

    @NotNull
    @EmailConstraint
    @XSSConstraint
    private String email;

    @NotNull
    @Min(value = DataConstraint.Company.COMPANY_MIN_NUM)
    private Integer employeeMaxNum;

    @XSSConstraint
    @NotBlank
    private String url;

    @NotNull
    private Integer sizeId;

    @NotNull
    private List<String> mediaUrls;

    @NotNull
    @Size(min = DataConstraint.JobPosition.CATEGORY_MIN, max = DataConstraint.JobPosition.CATEGORY_MAX)
    private List<Integer> benefitIds;

    @NotNull
    @Size(min = DataConstraint.Company.CATEGORY_MIN, max = DataConstraint.Company.CATEGORY_MAX)
    private List<Integer> subCategoriesIds;

}
