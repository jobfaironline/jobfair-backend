package org.capstone.job_fair.models.dtos.company.misc;


import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BenefitDTO implements Serializable {
    @EqualsAndHashCode.Include
    private Integer id;
    private String name;
    private String description;

    public BenefitDTO(int id) {
        this.id = id;
    }
}
