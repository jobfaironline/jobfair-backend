package org.capstone.job_fair.models.entities.attendant.cv;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cv_skill", schema = "dbo")
public class CvSkillEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "proficiency")
    private Integer proficiency;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cv_id")
    private CvEntity cv;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CvSkillEntity entity = (CvSkillEntity) o;
        return id != null && Objects.equals(id, entity.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
