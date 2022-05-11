package org.capstone.job_fair.models.dtos.job_fair.booth;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class JobFairBoothLayoutVideoDTO {
    @EqualsAndHashCode.Include
    private String id;
    private String url;
    private String itemName;
    private String jobFairBoothLayoutId;
}
