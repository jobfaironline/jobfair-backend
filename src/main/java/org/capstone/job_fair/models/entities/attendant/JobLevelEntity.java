package org.capstone.job_fair.models.entities.attendant;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "job_level", schema = "dbo")
public class JobLevelEntity {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 100)
    private String name;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobLevelEntity that = (JobLevelEntity) o;

        return !Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
