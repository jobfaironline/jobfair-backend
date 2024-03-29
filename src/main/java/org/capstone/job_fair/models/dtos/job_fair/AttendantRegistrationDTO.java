package org.capstone.job_fair.models.dtos.job_fair;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AttendantRegistrationDTO implements Serializable {

    private String id;

    private String attendantId;

    private String jobFairId;

    private Long createTime;

    private Boolean isVisit;

    private Long visitTime;

}
