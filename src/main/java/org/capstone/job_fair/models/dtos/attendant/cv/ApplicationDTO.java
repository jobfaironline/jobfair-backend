package org.capstone.job_fair.models.dtos.attendant.cv;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.company.job.BoothJobPositionDTO;
import org.capstone.job_fair.models.enums.ApplicationStatus;

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
    @JsonBackReference
    private CvDTO cvDTO;
    private ApplicationStatus status;
    @JsonBackReference
    private BoothJobPositionDTO boothJobPositionDTO;
    private AccountDTO authorizer;
    private String evaluateMessage;
    private Long evaluateDate;
}
