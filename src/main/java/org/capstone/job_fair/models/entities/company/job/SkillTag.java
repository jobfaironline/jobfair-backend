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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "skill_tag", schema = "dbo")
public class SkillTag {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id" ,nullable = false)
    private Integer id;
    @Column(name = "name", length = 100)
    private String name;

    @ManyToMany(mappedBy = "jobPositionSkillTags")
    List<JobPositionEntity> jobPositions;

}
