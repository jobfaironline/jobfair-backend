package org.capstone.job_fair.controllers.payload;

import lombok.*;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PasswordConstraint;
import org.capstone.job_fair.validators.PhoneConstraint;

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
    @NotBlank(message = "first name" + MessageConstant.InvalidFormat.NOT_BLANK_FORMAT)
    @Size(max = 100)
    private String firstName;
    @NotBlank(message = "last name" + MessageConstant.InvalidFormat.NOT_BLANK_FORMAT)
    @Size(max = 100)
    private String lastName;
    @Size(max = 100)
    private String middleName;
    @NotNull
    private Gender gender;
}
