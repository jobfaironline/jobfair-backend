package org.capstone.job_fair.models.dtos.company;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BenefitDTO implements Serializable {
    private Integer id;
    private String name;
    private String description;

    public BenefitDTO(int id) {
        this.id = id;
    }
}
