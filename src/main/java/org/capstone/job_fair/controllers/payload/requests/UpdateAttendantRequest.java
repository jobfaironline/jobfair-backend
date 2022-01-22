package org.capstone.job_fair.controllers.payload.requests;

import lombok.*;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.attendant.cv.*;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.models.enums.JobLevel;
import org.capstone.job_fair.models.enums.Marital;
import org.capstone.job_fair.models.enums.Qualification;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.utils.MessageUtil;
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
public class UpdateAttendantRequest {

    @NotNull
    @NotEmpty
    @XSSConstraint
    private String accountId;

    @Builder.Default
    @Valid
    private UpdateAttendantRequest.AccountRequest account = new UpdateAttendantRequest.AccountRequest();

    @XSSConstraint
    @PasswordConstraint
    private String password;


    @XSSConstraint
    @Size(min = DataConstraint.Attendant.TITLE_MIN, max = DataConstraint.Attendant.TITLE_MAX)
    private String title;

    @XSSConstraint
    @Size(min = DataConstraint.Attendant.ADDRESS_MIN, max = DataConstraint.Attendant.ADDRESS_MAX)
    private String address;

    @XSSConstraint
    @Size(min = DataConstraint.Attendant.JOB_TITLE_MIN, max = DataConstraint.Attendant.JOB_TITLE_MAX)
    private String jobTitle;

    @Min(value = DataConstraint.Attendant.MIN_DOB, message = MessageConstant.Account.DOB_INVALID)
    @Max(value = DataConstraint.Attendant.MAX_DOB, message = MessageConstant.Account.DOB_INVALID)
    private Long dob;

    @Min(value = DataConstraint.Attendant.YEAR_OF_EXPERIENCE_MIN, message = MessageConstant.Account.YEAR_OF_EXP_INVALID)
    @Max(value = DataConstraint.Attendant.YEAR_OF_EXPERIENCE_MAX, message = MessageConstant.Account.YEAR_OF_EXP_INVALID)
    private Double yearOfExp;

    private Marital maritalStatus;

    private String countryID;

    private String residenceID;

    private JobLevel jobLevel;

    @Valid
    private List<SkillRequest> skillRequests;

    @Valid
    private List<WorkHistoryRequest> workHistoryRequests;

    @Valid
    private List<EducationRequest> educationRequests;

    @Valid
    private List<CertificateRequest> certificateRequests;

    @Valid
    private List<ReferenceRequest> referenceRequests;

    @Valid
    private List<ActivityRequest> activityRequestList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class AccountRequest {
        @EmailConstraint
        private String email;
        @PhoneConstraint
        private String phone;
        @Size(min = DataConstraint.Account.MIN_IMAGE_URL, max = DataConstraint.Account.MAX_IMAGE_URL)
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
    public static class SkillRequest {
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
    public static class WorkHistoryRequest {
        @Size(min = DataConstraint.WorkHistory.POSITION_MIN_LENGTH, max =DataConstraint.WorkHistory.POSITION_MAX_LENGTH)
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
        @Size(min = DataConstraint.WorkHistory.MIN_DESCRIPTION, max = DataConstraint.WorkHistory.MAX_DESCRIPTION)
        private String description;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class EducationRequest {
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
    public static class CertificateRequest {
        @NotEmpty
        @XSSConstraint
        @Size(min = DataConstraint.Certification.NAME_MIN_LENGTH, max =DataConstraint.Certification.NAME_MAX_LENGTH)
        private String name;
        @XSSConstraint
        @Size(min = DataConstraint.Certification.INSTITUTION_MIN_LENGTH, max = DataConstraint.Certification.INSTITUTION_MAX_LENGTH)
        private String institution;
        @Min(value = DataConstraint.Certification.YEAR_MIN)
        private Integer year;
        @XSSConstraint
        @Size(min = DataConstraint.Certification.CERTIFICATION_LINK_MIN, max = DataConstraint.Certification.CERTIFICATION_LINK_MAX)
        private String certificationLink;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class ReferenceRequest {
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
    public static class ActivityRequest {
        @XSSConstraint
        @NotEmpty
        @Size(max = DataConstraint.Activity.MAX_NAME_LENGTH)
        private String name;
        @NotEmpty
        @XSSConstraint
        @Size(max = DataConstraint.Activity.FUNCTION_MAX_LENGTH)
        private String functionTitle;
        @NotEmpty
        @XSSConstraint
        @Size(max = DataConstraint.Activity.ORGANIZATION_MAX_LENGTH)
        private String organization;

        @Min(value = DataConstraint.Education.FROM_DATE)
        private Long fromDate;
        private Long toDate;
        private Boolean isCurrentActivity;

        @XSSConstraint
        @Size(min = DataConstraint.Activity.MIN_DESCRIPTION, max = DataConstraint.Activity.MAX_DESCRIPTION)
        private String description;
    }

}
