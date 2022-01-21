package org.capstone.job_fair.controllers.payload.requests;

import lombok.*;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.models.dtos.attendant.cv.*;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.models.enums.Marital;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PasswordConstraint;
import org.capstone.job_fair.validators.PhoneConstraint;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class AttendantRegisterRequest {

    @Builder.Default
    @Valid
    private AttendantRegisterRequest.AccountRequest account = new AttendantRegisterRequest.AccountRequest();

    @XSSConstraint
    @NotEmpty
    @PasswordConstraint
    private String password;

    @XSSConstraint
    @NotEmpty
    @PasswordConstraint
    private String confirmPassword;

    @NotNull
    @XSSConstraint
    private String title;

    @NotNull
    @XSSConstraint
    private String address;

    @NotNull
    @XSSConstraint
    private String jobTitle;

    private Long dob;

    private Double yearOfExp;

    private Marital maritalStatus;

    @NotNull
    @XSSConstraint
    private String countryID;

    @NotNull
    @XSSConstraint
    private String residenceID;

    private Integer jobLevelID;

    @NotNull
    @Valid
    private List<SkillRequest> skillRequests;

    @NotNull
    @Valid
    private List<WorkHistoryRequest> workHistoryRequests;

    @NotNull
    @Valid
    private List<EducationRequest> educationRequests;

    @NotNull
    @Valid
    private List<CertificateRequest> certificateRequests;

    @NotNull
    @Valid
    private List<ReferenceRequest> referenceRequests;

    @NotNull
    @Valid
    private List<ActivityRequest> activityRequestList;

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
        @NotNull
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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class SkillRequest {
        @NotNull
        @XSSConstraint
        private String name;
        private Integer proficiency;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class WorkHistoryRequest {
        @NotNull
        @XSSConstraint
        private String position;
        @NotNull
        @XSSConstraint
        private String company;

        private Long fromDate;
        private Long toDate;
        private Boolean isCurrentJob;
        @NotNull
        @XSSConstraint
        private String description;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class EducationRequest {
        @NotEmpty
        @XSSConstraint
        private String subject;
        @XSSConstraint
        @NotNull
        private String school;
        private Long fromDate;
        private Long toDate;
        @XSSConstraint
        @NotNull
        private String achievement;
        private Integer qualificationId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class CertificateRequest {
        @NotEmpty
        @XSSConstraint
        private String name;
        @XSSConstraint
        @NotNull
        private String institution;
        private Integer year;
        @XSSConstraint
        @NotNull
        private String certificationLink;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class ReferenceRequest {
        @XSSConstraint
        @NotNull
        private String fullname;
        @XSSConstraint
        @NotNull
        private String position;
        @XSSConstraint
        @NotNull
        private String company;
        @XSSConstraint
        @NotNull
        @EmailConstraint
        private String email;
        @XSSConstraint
        @NotNull
        @PhoneConstraint
        private String phone;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class ActivityRequest {
        @NotEmpty
        @XSSConstraint
        private String name;
        @NotEmpty
        @XSSConstraint
        private String functionTitle;
        @NotEmpty
        @XSSConstraint
        private String organization;

        private Long fromDate;
        private Long toDate;
        private Boolean isCurrentActivity;

        @NotNull
        @XSSConstraint
        private String description;
    }

}
