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
@Table(name = "country", schema = "dbo")
public class CountryEntity {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name", length = 1000)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CountryEntity)) return false;
        CountryEntity country = (CountryEntity) o;
        return getId().equals(country.getId()) && getName().equals(country.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
