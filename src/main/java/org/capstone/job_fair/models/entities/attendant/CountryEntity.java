package org.capstone.job_fair.models.entities.attendant;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "country", schema = "dbo")
public class CountryEntity {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 1000)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CountryEntity entity = (CountryEntity) o;
        return id != null && Objects.equals(id, entity.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
