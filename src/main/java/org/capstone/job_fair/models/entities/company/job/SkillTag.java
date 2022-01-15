package org.capstone.job_fair.models.entities.company.job;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "skill_tag", schema = "dbo")
public class SkillTag {
    @Id
    @Column(name = "id" ,nullable = false)
    private Integer id;
    @Column(name = "name", length = 100)
    private String name;

    @ManyToMany(mappedBy = "jobPositionSkillTags")
    List<JobPositionEntity> jobPositions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SkillTag)) return false;
        SkillTag skillTag = (SkillTag) o;
        return getId().equals(skillTag.getId()) && getName().equals(skillTag.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
