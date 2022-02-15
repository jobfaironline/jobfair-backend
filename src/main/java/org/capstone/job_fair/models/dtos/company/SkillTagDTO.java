package org.capstone.job_fair.models.dtos.company;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SkillTagDTO implements Serializable {
    @EqualsAndHashCode.Include
    private Integer id;
    private String name;

    public SkillTagDTO(int id) {
        this.id = id;
    }

}
