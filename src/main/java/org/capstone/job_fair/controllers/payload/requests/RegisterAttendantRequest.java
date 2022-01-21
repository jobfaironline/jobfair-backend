package org.capstone.job_fair.controllers.payload.requests;

import lombok.*;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.models.enums.JobLevel;
import org.capstone.job_fair.models.enums.Marital;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PasswordConstraint;
import org.capstone.job_fair.validators.PhoneConstraint;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@ToString
public class RegisterAttendantRequest {

    @Builder.Default
    @Valid
    private AccountRequest account = new AccountRequest();
    @XSSConstraint
    @NotEmpty
    @PasswordConstraint
    private String password;
    @XSSConstraint
    @NotEmpty
    @PasswordConstraint
    private String confirmPassword;
    @NotEmpty
    @Size(max = DataConstraint.Attendant.TITTLE_LENGTH)
    @XSSConstraint
    private String title;
    @Size(max = DataConstraint.Attendant.ADDRESS_LENGTH)
    @XSSConstraint
    @NotEmpty
    private String address;
    private Long dob;
    @Size(max = DataConstraint.Attendant.JOB_TITTLE_LENGTH)
    @XSSConstraint
    private String jobTitle;
    @Min(DataConstraint.Attendant.YEAR_OF_EXPERIENCE_MIN)
    private Double yearOfExp;
    private Marital maritalStatus;
    private String country ;
    private String residence;

    private JobLevel currentJobLevel;


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
        private String profileImageUrl;
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
