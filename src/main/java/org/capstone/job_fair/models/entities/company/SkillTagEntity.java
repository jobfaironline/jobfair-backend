package org.capstone.job_fair.models.entities.company;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "skill_tag", schema = "dbo")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SkillTagEntity implements Serializable {
    public SkillTagEntity(int id) {
        this.id = id;
    }

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "name", nullable = true, length = 100)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SkillTagEntity that = (SkillTagEntity) o;

        return Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
