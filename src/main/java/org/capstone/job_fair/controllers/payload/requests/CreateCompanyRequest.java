package org.capstone.job_fair.controllers.payload.requests;

import lombok.*;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PhoneConstraint;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.Valid;
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
    @NotBlank(message = MessageConstant.InvalidFormatValidationMessage.NOT_BLANK_FORMAT)
    private String taxId;

    @NotNull
    @Size(max = DataConstraint.Company.NAME_MAX_LENGTH)
    private String name;

    @NotNull
    @Size(max = DataConstraint.Company.ADDRESS_MAX_LENGTH)
    @XSSConstraint
    private String address;

    @NotNull
    @PhoneConstraint
    private String phone;

    @NotNull
    @EmailConstraint
    @XSSConstraint
    private String email;

    @XSSConstraint
    @NotNull
    private String url;


    private Integer sizeId;

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
