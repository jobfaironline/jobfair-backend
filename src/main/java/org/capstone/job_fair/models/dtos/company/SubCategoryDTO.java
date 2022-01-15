package org.capstone.job_fair.models.dtos.company;

import lombok.*;
import org.capstone.job_fair.models.entities.company.ProfessionEntity;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SubCategoryDTO implements Serializable {
    public SubCategoryDTO(int id) {
        this.id = id;
    }

    private int id;
    private String name;

    private ProfessionCategoryDTO category;
}
