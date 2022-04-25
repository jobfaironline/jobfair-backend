package org.capstone.job_fair.controllers.payload.requests.company;

import lombok.*;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PasswordConstraint;
import org.capstone.job_fair.validators.PhoneConstraint;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CompanyManagerRegisterRequest {
    @EmailConstraint
    private String email;
    @PasswordConstraint
    private String password;
    @PasswordConstraint
    private String confirmPassword;
    @PhoneConstraint
    private String phone;
    @NotBlank(message = MessageConstant.InvalidFormatValidationMessage.NOT_BLANK_FORMAT)
    @Size(max = DataConstraint.Account.NAME_LENGTH)
    @XSSConstraint
    private String firstName;
    @NotBlank(message = MessageConstant.InvalidFormatValidationMessage.NOT_BLANK_FORMAT)
    @Size(max = DataConstraint.Account.NAME_LENGTH)
    @XSSConstraint
    private String lastName;
    @XSSConstraint
    @Size(max = DataConstraint.Account.NAME_LENGTH)
    private String middleName;
    @NotNull
    private Gender gender;
    @NotNull
    private String companyId;
    @NotBlank
    private String department;
}
