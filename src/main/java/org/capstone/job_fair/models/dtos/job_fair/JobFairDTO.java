package org.capstone.job_fair.models.dtos.job_fair;

import lombok.*;
import org.capstone.job_fair.models.statuses.JobFairStatus;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class JobFairDTO implements Serializable {

    private String id;

    private long companyRegisterStartTime;

    private long companyRegisterEndTime;

    private long companyBuyBoothStartTime;

    private long companyBuyBoothEndTime;

    private long attendantRegisterStartTime;

    private Long startTime;

    private long endTime;

    private String description;

    private String layoutId;

    private JobFairStatus status;

    private String creatorId;

    private String authorizerId;
}
