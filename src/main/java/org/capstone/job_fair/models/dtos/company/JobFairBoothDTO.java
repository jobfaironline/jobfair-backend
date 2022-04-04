package org.capstone.job_fair.models.dtos.company;

import lombok.*;
import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;
import org.capstone.job_fair.models.dtos.job_fair.LayoutBoothDTO;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class JobFairBoothDTO implements Serializable {
    @EqualsAndHashCode.Include
    private String id;
    private JobFairDTO jobFair;
    private LayoutBoothDTO layoutBoothDTO;
}