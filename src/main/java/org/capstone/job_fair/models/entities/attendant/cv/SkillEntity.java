package org.capstone.job_fair.models.entities.attendant.cv;

import org.capstone.job_fair.models.entities.attendant.AttendantEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "skill", schema = "dbo")
public class SkillEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;
    @Basic
    @Column(name = "name", nullable = true, length = 100)
    private String name;

    @Basic
    @Column(name = "proficiency")
    private Integer proficiency;

    @ManyToOne
    @JoinColumn(name = "attendant_id")
    private AttendantEntity attendant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SkillEntity)) return false;
        SkillEntity that = (SkillEntity) o;
        return id.equals(that.id) && name.equals(that.name) && proficiency.equals(that.proficiency) && attendant.equals(that.attendant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, proficiency, attendant);
    }
}
