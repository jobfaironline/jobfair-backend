package org.capstone.job_fair.models.entities.company.job.questions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.entities.company.job.BoothJobPositionEntity;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "choices", schema = "dbo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChoicesEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column(name = "content")
    private String content;
    @Column(name = "isCorrect")
    private boolean isCorrect;
    @Column(name = "questionId")
    private String questionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BoothJobPositionEntity that = (BoothJobPositionEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
