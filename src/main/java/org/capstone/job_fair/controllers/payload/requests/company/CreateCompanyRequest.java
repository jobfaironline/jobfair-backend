package org.capstone.job_fair.controllers.payload.requests.company;

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
    private String url;

    @XSSConstraint
    private String companyDescription;

    private Integer sizeId;

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
