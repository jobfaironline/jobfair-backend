package org.capstone.job_fair.models.entities.attendant.cv;

import lombok.*;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "skill", schema = "dbo")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SkillEntity {
    @EqualsAndHashCode.Include
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

}
