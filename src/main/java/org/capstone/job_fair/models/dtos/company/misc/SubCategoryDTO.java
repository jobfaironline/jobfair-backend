package org.capstone.job_fair.models.dtos.company.misc;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SubCategoryDTO implements Serializable {
    @EqualsAndHashCode.Include
    private int id;
    private String name;
    private ProfessionCategoryDTO category;

    public SubCategoryDTO(int id) {
        this.id = id;
    }
}