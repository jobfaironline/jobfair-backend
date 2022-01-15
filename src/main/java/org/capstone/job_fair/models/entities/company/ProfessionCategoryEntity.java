package org.capstone.job_fair.models.entities.company;

import lombok.*;

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
    private int id;
    @Basic
    @Column(name = "name", length = 100)
    private String name;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProfessionCategoryEntity that = (ProfessionCategoryEntity) o;

        return !Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}