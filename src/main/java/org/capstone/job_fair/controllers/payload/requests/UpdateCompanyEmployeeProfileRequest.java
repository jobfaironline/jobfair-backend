package org.capstone.job_fair.controllers.payload.requests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PhoneConstraint;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateCompanyEmployeeProfileRequest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class AccountRequest {
        @EmailConstraint
        private String email;
        private AccountStatus status;
        @PhoneConstraint
        private String phone;
        @NotEmpty
        @XSSConstraint
        private String profileImageUrl;
        @NotNull
        @NotEmpty
        @Size(max = DataConstraint.Account.NAME_LENGTH)
        @XSSConstraint
        private String firstname;
        @NotNull
        @NotEmpty
        @Size(max = DataConstraint.Account.NAME_LENGTH)
        @XSSConstraint
        private String lastname;
        @NotNull
        @NotEmpty
        @Size(max = DataConstraint.Account.NAME_LENGTH)
        @XSSConstraint
        private String middlename;

        private Gender gender;
    }

    private String accountId;
    private AccountRequest accountRequest;
    private String companyId;
}
