package org.capstone.job_fair.controllers.payload.requests;

import lombok.*;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PasswordConstraint;
import org.capstone.job_fair.validators.PhoneConstraint;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@ToString
public class RegisterAttendantRequest {

    @Valid
    private RegisterAttendantRequest.AccountRequest account;
    @XSSConstraint
    @NotEmpty
    @PasswordConstraint
    private String password;
    @XSSConstraint
    @NotEmpty
    @PasswordConstraint
    private String confirmPassword;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class AccountRequest {
        @EmailConstraint
        private String email;
        @PhoneConstraint
        private String phone;
        @NotEmpty
        @Size(max = DataConstraint.Account.NAME_LENGTH)
        @XSSConstraint
        @NotEmpty
        private String firstname;
        @Size(max = DataConstraint.Account.NAME_LENGTH)
        @XSSConstraint
        @NotEmpty
        private String lastname;
        @Size(max = DataConstraint.Account.NAME_LENGTH)
        @XSSConstraint
        @NotEmpty
        private String middlename;
        private Gender gender;
    }
}
