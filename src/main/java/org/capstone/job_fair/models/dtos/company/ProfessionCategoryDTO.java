package org.capstone.job_fair.models.dtos.company;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProfessionCategoryDTO implements Serializable {
    @EqualsAndHashCode.Include
    private int id;
    private String name;
}
