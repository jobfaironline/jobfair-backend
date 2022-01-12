package org.capstone.job_fair.models.dtos.attendant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.dtos.account.AccountDTO;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendantDTO implements Serializable {
    private String accountId;
    private AccountDTO account;
    private String title;
    private String address;
    private Long dob;
    private String jobTitle;
    private Double yearOfExp;
    private Boolean maritalStatus;
    private CountryDTO country;
    private ResidenceDTO residence;
    private JobLevelDTO currentJobLevel;
}
