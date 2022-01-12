package org.capstone.job_fair.controllers.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.capstone.job_fair.models.entities.account.AccountEntity;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AttendantRequest {
    private String accountId;
    private AccountRequest account;
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
    private CountryRequest country;
    private ResidenceRequest residence;
    private JobLevelRequest currentJobLevel;
}
