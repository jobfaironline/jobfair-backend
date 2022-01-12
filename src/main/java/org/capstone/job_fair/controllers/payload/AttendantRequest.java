package org.capstone.job_fair.controllers.payload;

import lombok.*;
import org.capstone.job_fair.models.entities.account.AccountEntity;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@ToString
public class AttendantRequest {
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
    private Boolean maritalStatus;
    @Builder.Default
    private CountryRequest country = new CountryRequest();
    @Builder.Default
    private ResidenceRequest residence = new ResidenceRequest();
    @Builder.Default
    private JobLevelRequest currentJobLevel = new JobLevelRequest();
}
