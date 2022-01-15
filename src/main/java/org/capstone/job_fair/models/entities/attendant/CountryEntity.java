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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "country", schema = "dbo")
public class CountryEntity {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name", length = 1000)
    private String name;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


}
