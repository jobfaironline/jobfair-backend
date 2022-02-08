package org.capstone.job_fair.models.entities.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "registration_job_skill_tag", schema = "dbo", catalog = "")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RegistrationJobSkillTagEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "skill_tag_id")
    private int skillTagId;
    @Column(name = "registration_job_position_id")
    private String registrationJobPositionId;


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
