package org.capstone.job_fair.controllers.payload.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PasswordConstraint;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AttendantRegisterRequest {
    @EmailConstraint
    private String email;
    @PasswordConstraint
    private String password;
    @PasswordConstraint
    private String confirmPassword;
    @NotNull
    @NotEmpty
    @Size(max = DataConstraint.Account.NAME_LENGTH)
    @XSSConstraint
    private String firstName;
    @Size(max = DataConstraint.Account.NAME_LENGTH)
    @NotBlank
    @XSSConstraint
    private String middleName;
    @NotEmpty
    @Size(max = DataConstraint.Account.NAME_LENGTH)
    @XSSConstraint
    private String lastName;
    @NotNull
    private Gender gender;
}
