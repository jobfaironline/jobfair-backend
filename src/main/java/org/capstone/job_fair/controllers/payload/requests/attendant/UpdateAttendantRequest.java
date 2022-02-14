package org.capstone.job_fair.controllers.payload.requests.attendant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.models.enums.JobLevel;
import org.capstone.job_fair.models.enums.Marital;
import org.capstone.job_fair.models.enums.Qualification;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PasswordConstraint;
import org.capstone.job_fair.validators.PhoneConstraint;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor

public class UpdateAttendantRequest {

    @NotNull
    private String accountId;

    @Valid
    private AccountRequest account;

    @XSSConstraint
    @Size(min = DataConstraint.Attendant.TITLE_MIN_LENGTH, max = DataConstraint.Attendant.TITLE_MAX_LENGTH)
    private String title;

    @XSSConstraint
    @Size(min = DataConstraint.Attendant.ADDRESS_MIN_LENGTH, max = DataConstraint.Attendant.ADDRESS_MAX_LENGTH)
    private String address;

    @XSSConstraint
    @Size(min = DataConstraint.Attendant.JOB_TITLE_MIN_LENGTH, max = DataConstraint.Attendant.JOB_TITLE_MAX_LENGTH)
    private String jobTitle;

    @Min(value = DataConstraint.Attendant.MIN_DOB, message = MessageConstant.Account.DOB_INVALID)
    @Max(value = DataConstraint.Attendant.MAX_DOB, message = MessageConstant.Account.DOB_INVALID)
    private Long dob;

    @Min(value = DataConstraint.Attendant.YEAR_OF_EXPERIENCE_MIN, message = MessageConstant.Account.YEAR_OF_EXP_INVALID)
    @Max(value = DataConstraint.Attendant.YEAR_OF_EXPERIENCE_MAX, message = MessageConstant.Account.YEAR_OF_EXP_INVALID)
    private Double yearOfExp;

    private Marital maritalStatus;

    private String countryId;

    private String residenceId;

    private JobLevel jobLevel;

    @Valid
    private List<Skills> skills;

    @Valid
    private List<WorkHistories> workHistories;

    @Valid
    private List<Educations> educations;

    @Valid
    private List<Certifications> certifications;

    @Valid
    private List<References> references;

    @Valid
    private List<Activities> activities;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class AccountRequest {
        @EmailConstraint
        private String email;
        @PasswordConstraint
        private String password;
        @PhoneConstraint
        private String phone;
        @Size(min = DataConstraint.Account.URL_MIN_LENGTH, max = DataConstraint.Account.URL_MAX_LENGTH)
        @XSSConstraint
        private String profileImageUrl;
        @Size(max = DataConstraint.Account.NAME_LENGTH)
        @XSSConstraint
        private String firstname;
        @Size(max = DataConstraint.Account.NAME_LENGTH)
        @XSSConstraint
        private String lastname;
        @Size(max = DataConstraint.Account.NAME_LENGTH)
        @XSSConstraint
        private String middlename;
        private Gender gender;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Skills {
        private String id;
        @XSSConstraint
        @Size(min = DataConstraint.Skill.MIN_NAME_LENGTH, max = DataConstraint.Skill.MAX_NAME_LENGTH)
        private String name;
        @Min(DataConstraint.Skill.MIN_PROFICIENCY)
        @Max(DataConstraint.Skill.MAX_PROFICIENCY)
        private Integer proficiency;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class WorkHistories {
        private String id;
        @Size(min = DataConstraint.WorkHistory.POSITION_MIN_LENGTH, max = DataConstraint.WorkHistory.POSITION_MAX_LENGTH)
        @XSSConstraint
        private String position;
        @XSSConstraint
        @Size(min = DataConstraint.WorkHistory.COMPANY_MIN_LENGTH, max = DataConstraint.WorkHistory.COMPANY_MAX_LENGTH)
        private String company;

        @Min(value = DataConstraint.WorkHistory.FROM_DATE)
        private Long fromDate;
        @Min(value = DataConstraint.WorkHistory.FROM_DATE)
        private Long toDate;
        private Boolean isCurrentJob;
        @XSSConstraint
        @Size(min = DataConstraint.WorkHistory.MIN_DESCRIPTION_LENGTH, max = DataConstraint.WorkHistory.MAX_DESCRIPTION_LENGTH)
        private String description;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Educations {
        private String id;
        @XSSConstraint
        @Size(min = DataConstraint.Education.SUBJECT_MIN_LENGTH, max = DataConstraint.Education.SUBJECT_MAX_LENGTH)
        private String subject;
        @XSSConstraint
        @Size(min = DataConstraint.Education.SCHOOL_MIN_LENGTH, max = DataConstraint.Education.SCHOOL_MAX_LENGTH)
        private String school;
        @Min(value = DataConstraint.Education.FROM_DATE)
        private Long fromDate;
        @Min(value = DataConstraint.Education.FROM_DATE)
        private Long toDate;
        @XSSConstraint
        @Size(min = DataConstraint.Education.ACHIEVEMENT_MIN_LENGTH, max = DataConstraint.Education.ACHIEVEMENT_MAX_LENGTH)
        private String achievement;
        private Qualification qualification;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Certifications {
        private String id;
        @NotEmpty
        @XSSConstraint
        @Size(min = DataConstraint.Certification.NAME_MIN_LENGTH, max = DataConstraint.Certification.NAME_MAX_LENGTH)
        private String name;
        @XSSConstraint
        @Size(min = DataConstraint.Certification.INSTITUTION_MIN_LENGTH, max = DataConstraint.Certification.INSTITUTION_MAX_LENGTH)
        private String institution;
        @Min(value = DataConstraint.Certification.YEAR_MIN)
        private Integer year;
        @XSSConstraint
        @Size(min = DataConstraint.Certification.URL_MIN_LENGTH, max = DataConstraint.Certification.URL_MAX_LENGTH)
        private String certificationLink;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class References {
        private String id;
        @XSSConstraint
        @Size(min = DataConstraint.Reference.FULLNAME_MIN_LENGTH, max = DataConstraint.Reference.FULLNAME_MAX_LENGTH)
        private String fullname;
        @XSSConstraint
        @Size(min = DataConstraint.Reference.POSITION_MIN_LENGTH, max = DataConstraint.Reference.POSITION_MAX_LENGTH)
        private String position;
        @XSSConstraint
        @Size(min = DataConstraint.Reference.COMPANY_MIN_LENGTH, max = DataConstraint.Reference.COMPANY_MAX_LENGTH)
        private String company;
        @EmailConstraint
        private String email;
        @PhoneConstraint
        private String phone;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Activities {
        private String id;
        @XSSConstraint
        @Size(min = DataConstraint.Activity.MIN_NAME_LENGTH, max = DataConstraint.Activity.MAX_NAME_LENGTH)
        private String name;
        @XSSConstraint
        @Size(min = DataConstraint.Activity.FUNCTION_MIN_LENGTH, max = DataConstraint.Activity.FUNCTION_MAX_LENGTH)
        private String functionTitle;
        @XSSConstraint
        @Size(min = DataConstraint.Activity.ORGANIZATION_MIN_LENGTH, max = DataConstraint.Activity.ORGANIZATION_MAX_LENGTH)
        private String organization;

        @Min(value = DataConstraint.Education.FROM_DATE)
        private Long fromDate;
        private Long toDate;
        private Boolean isCurrentActivity;

        @XSSConstraint
        @Size(min = DataConstraint.Activity.MIN_DESCRIPTION_LENGTH, max = DataConstraint.Activity.MAX_DESCRIPTION_LENGTH)
        private String description;
    }

}
