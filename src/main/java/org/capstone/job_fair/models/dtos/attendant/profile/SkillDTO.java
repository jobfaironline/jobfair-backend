package org.capstone.job_fair.models.dtos.attendant.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillDTO {
    private String id;
    private String name;
    private Integer proficiency;
}
