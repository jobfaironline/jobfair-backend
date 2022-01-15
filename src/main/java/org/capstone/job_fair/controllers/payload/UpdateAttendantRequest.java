package org.capstone.job_fair.controllers.payload;

import lombok.*;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.models.enums.Marital;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PhoneConstraint;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@ToString
public class UpdateAttendantRequest {

    @NotNull
    private String accountId;
    @Builder.Default
    private AccountRequest account = new AccountRequest();
    @Size(max = 100)
    @Pattern(message="Type can contain alphanumeric characters only", regexp = "[a-zA-Z0-9 ]+")
    private String title;
    @Size(max = 100)
    @Pattern(message="Type can contain alphanumeric characters only", regexp = "[a-zA-Z0-9 ]+")
    private String address;
    private Long dob;
    @Size(max = 100)
    @Pattern(message="Type can contain alphanumeric characters only", regexp = "[a-zA-Z0-9 ]+")
    private String jobTitle;
    private Double yearOfExp;
    private Marital maritalStatus;
    private String country ;
    private String residence;
    private String currentJobLevel;


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
        private String profileImageUrl;
        @NotEmpty
        @Size(max = 100)
        @Pattern(message="Type can contain alphanumeric characters only", regexp = "[a-zA-Z0-9 ]+")
        private String firstname;
        @Size(max = 100)
        @Pattern(message="Type can contain alphanumeric characters only", regexp = "[a-zA-Z0-9 ]+")
        private String lastname;
        @Size(max = 100)
        @Pattern(message="Type can contain alphanumeric characters only", regexp = "[a-zA-Z0-9 ]+")
        private String middlename;
        private Gender gender;
    }
}
