package org.capstone.job_fair.models.dtos.company;

import lombok.*;
import org.capstone.job_fair.models.dtos.company.job.BoothJobPositionDTO;
import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;
import org.capstone.job_fair.models.dtos.job_fair.LayoutBoothDTO;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class JobFairBoothDTO implements Serializable {
    @EqualsAndHashCode.Include
    private String id;
    private String description;
    private JobFairDTO jobFair;
    private LayoutBoothDTO booth;
    private String name;
    private List<BoothJobPositionDTO> boothJobPositions;
}
