package org.capstone.job_fair.models.entities.company;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "sub_category", schema = "dbo")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SubCategoryEntity implements Serializable {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubCategoryEntity that = (SubCategoryEntity) o;

        return !Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        return result;
    }
}
