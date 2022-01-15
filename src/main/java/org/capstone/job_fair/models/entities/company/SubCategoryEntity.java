package org.capstone.job_fair.models.entities.company;

import lombok.*;
import org.capstone.job_fair.models.entities.company.job.JobPositionEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "sub_category", schema = "dbo")
public class SubCategoryEntity {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "name", length = 100)
    private String name;

    public SubCategoryEntity(int id){
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProfessionCategoryEntity category;

}
