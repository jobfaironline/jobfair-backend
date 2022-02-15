package org.capstone.job_fair.models.dtos.company.job;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class JobTypeDTO {
    @EqualsAndHashCode.Include
    private int id;
    private String name;
}
