package org.capstone.job_fair.models.dtos.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkillTagDTO implements Serializable {
    public SkillTagDTO(int id) {
        this.id = id;
    }

    private Integer id;
    private String name;
}
