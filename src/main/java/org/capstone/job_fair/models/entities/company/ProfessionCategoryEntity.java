package org.capstone.job_fair.models.entities.company;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "profession_category", schema = "dbo")
public class ProfessionCategoryEntity implements Serializable {
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "name", length = 100)
    private String name;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProfessionCategoryEntity that = (ProfessionCategoryEntity) o;
        return !Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
