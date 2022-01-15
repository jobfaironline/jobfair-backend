package org.capstone.job_fair.models.entities.attendant;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "qualification", schema = "dbo")
public class QualificationEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "name", length = 100)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QualificationEntity)) return false;
        QualificationEntity that = (QualificationEntity) o;
        return getId().equals(that.getId()) && getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
