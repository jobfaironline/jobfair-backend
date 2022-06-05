package org.capstone.job_fair.models.dtos.job_fair;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ShiftDTO implements Serializable {
    private String id;
    private Long beginTime;
    private Long endTime;
    private String jobFairId;
}
