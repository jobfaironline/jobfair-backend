package org.capstone.job_fair.models.entities.company;

import lombok.*;
import org.capstone.job_fair.models.entities.company.job.JobPositionEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "sub_category", schema = "dbo")
public class SubCategoryEntity {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private int id;
    @Column(name = "name", length = 1000)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ManyToMany(mappedBy = "companySubCategory")
    List<CompanyEntity> companies;

    @ManyToMany(mappedBy = "jobCategories")
    List<JobPositionEntity> jobPositions;

}
