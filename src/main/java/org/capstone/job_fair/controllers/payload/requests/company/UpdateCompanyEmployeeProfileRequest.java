package org.capstone.job_fair.controllers.payload.requests.company;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.NameConstraint;
import org.capstone.job_fair.validators.PhoneConstraint;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.Valid;
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
        @XSSConstraint
        private String profileImageUrl;
        @Size(max = DataConstraint.Account.NAME_LENGTH)
        @XSSConstraint
        @NameConstraint
        private String firstname;
        @Size(max = DataConstraint.Account.NAME_LENGTH)
        @XSSConstraint
        @NameConstraint
        private String lastname;
        @Size(max = DataConstraint.Account.NAME_LENGTH)
        @XSSConstraint
        @NameConstraint
        private String middlename;


        private Gender gender;
    }

    @Valid
    private AccountRequest accountRequest;
}
