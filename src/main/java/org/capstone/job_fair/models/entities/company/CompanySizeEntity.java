package org.capstone.job_fair.models.entities.company;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "company_size", schema = "dbo")
public class CompanySizeEntity {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompanySizeEntity)) return false;
        CompanySizeEntity that = (CompanySizeEntity) o;
        return getId().equals(that.getId()) && getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
