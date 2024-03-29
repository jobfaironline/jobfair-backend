package org.capstone.job_fair.models.entities.company.misc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sub_category", schema = "dbo")
public class SubCategoryEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "name", length = 100)
    private String name;

    public SubCategoryEntity(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ProfessionCategoryEntity category;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SubCategoryEntity entity = (SubCategoryEntity) o;
        return id != null && Objects.equals(id, entity.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
