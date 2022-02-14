package org.capstone.job_fair.models.entities.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
