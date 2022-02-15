package org.capstone.job_fair.models.dtos.company;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class IndustryDTO {
    @EqualsAndHashCode.Include
    private String id;
    private String name;
    private String description;
}
