package org.capstone.job_fair.controllers.payload;

import lombok.*;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.attendant.cv.*;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.models.statuses.CvStatus;
import org.capstone.job_fair.models.statuses.MaritalStatus;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PhoneConstraint;

import javax.validation.constraints.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CreateCvRequest {
    private static final int MIN_YEAR_OF_EXP = 1;

    @Builder.Default
    private CreateCvRequest.AccountRequest account = new CreateCvRequest.AccountRequest();

    @NotBlank(message = "first name " + MessageConstant.InvalidFormat.NOT_BLANK_FORMAT)
    @Size(max = 100)
    @Pattern(message="Type can contain alphanumeric characters only", regexp = "[a-zA-Z0-9 ]+")
    private String firstname;

    @NotBlank(message = "last name " + MessageConstant.InvalidFormat.NOT_BLANK_FORMAT)
    @Size(max = 100)
    @Pattern(message="Type can contain alphanumeric characters only", regexp = "[a-zA-Z0-9 ]+")
    private String lastname;

    @Size(max = 100)
    @Pattern(message="Type can contain alphanumeric characters only", regexp = "[a-zA-Z0-9 ]+")
    private String middlename;

    @Min(value = MIN_YEAR_OF_EXP, message = MessageConstant.InvalidFormat.MIN_YEAR_OF_EXP_INVALID)
    private Double yearOfExp;

    @EmailConstraint
    private String email;

    @PhoneConstraint
    private String phone;

    private Long dob;

    private String address;

    private String summary;

    private Long createDate;

    @NotNull
    private String nationalityId;

    private MaritalStatus maritalStatus;

    @NotNull
    private String countryId;
    @NotNull
    private Integer jobLevelId;
    @NotNull
    private Gender gender;
    @NotNull
    private String accountId;
    @NotNull
    private CvStatus status;

    private List<SkillDTO> skills;
    private List<WorkHistoryDTO> histories;
    private List<EducationDTO> educations;
    private List<CertificationDTO> certifications;
    private List<ReferenceDTO> references;
    private List<ActivityDTO> activities;


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
