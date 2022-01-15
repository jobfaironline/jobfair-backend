package org.capstone.job_fair.models.entities.company;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "skill_tag", schema = "dbo")
@AllArgsConstructor
@NoArgsConstructor
public class SkillTagEntity implements Serializable {
    public SkillTagEntity(int id){
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

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        int result = id == null ? 0 : id.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
