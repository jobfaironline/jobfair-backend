package org.capstone.job_fair.models.entities.company;

import org.capstone.job_fair.models.entities.company.job.JobPositionEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "sub_category", schema = "dbo")
public class SubCategoryEntity {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubCategoryEntity)) return false;
        SubCategoryEntity that = (SubCategoryEntity) o;
        return id == that.id && name.equals(that.name) && category.equals(that.category) && companies.equals(that.companies) && jobPositions.equals(that.jobPositions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, companies, jobPositions);
    }
}
