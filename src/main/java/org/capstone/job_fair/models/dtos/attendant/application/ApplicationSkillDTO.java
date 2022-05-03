package org.capstone.job_fair.models.dtos.attendant.application;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ApplicationSkillDTO {
    @EqualsAndHashCode.Include
    private String id;
    private String name;
    private Integer proficiency;
}
