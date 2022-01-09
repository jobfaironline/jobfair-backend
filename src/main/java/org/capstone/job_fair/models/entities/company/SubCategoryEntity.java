package org.capstone.job_fair.models.entities.company;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "sub_category", schema = "dbo", catalog = "")
public class SubCategoryEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;
    @Basic
    @Column(name = "name", nullable = true, length = 1000)
    private String name;
    @Basic
    @Column(name = "description", nullable = true, length = 5000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubCategoryEntity that = (SubCategoryEntity) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(description, that.description)) return false;
        if (!Objects.equals(category, that.category)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        return result;
    }
}
