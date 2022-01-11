package org.capstone.job_fair.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PasswordConstraint;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AttendantRegisterRequest {
    @EmailConstraint
    private String email;
    @PasswordConstraint
    private String password;
    @NotNull
    @NotEmpty
    @Size(min = 2)
    private String confirmPassword;
    @NotNull
    @NotEmpty
    private String firstName;
    private String middleName;
    @NotNull
    @NotEmpty
    private String lastName;
    @NotNull
    private Gender gender;
}
