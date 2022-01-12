package org.capstone.job_fair.models.dtos.attendant;

import lombok.*;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.attendant.CountryEntity;
import org.capstone.job_fair.models.entities.attendant.JobLevelEntity;
import org.capstone.job_fair.models.entities.attendant.ResidenceEntity;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AttendantDTO {
    private String accountId;
    private AccountEntity account;
    private String title;
    private String address;
    private Long dob;
    private String jobTitle;
    private Double yearOfExp;
    private Boolean maritalStatus;
    private CountryEntity country;
    private ResidenceEntity residence;
    private JobLevelEntity currentJobLevel;
}
