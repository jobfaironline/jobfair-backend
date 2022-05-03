package org.capstone.job_fair.models.dtos.attendant.application;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.dtos.company.job.BoothJobPositionDTO;
import org.capstone.job_fair.models.enums.ApplicationStatus;
import org.capstone.job_fair.models.enums.JobLevel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ApplicationDTO {
    @EqualsAndHashCode.Include
    private String id;
    private String summary;
    private Long createDate;
    private ApplicationStatus status;
    @JsonBackReference
    private BoothJobPositionDTO boothJobPositionDTO;
    private AccountDTO authorizer;
    private String evaluateMessage;
    private Long evaluateDate;
    private String email;
    private String phone;
    private Integer yearOfExp;
    private String jobTitle;
    private JobLevel jobLevel;
    private AttendantDTO attendant;
    private String originCvId;


}
