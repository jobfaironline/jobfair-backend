package org.capstone.job_fair.models.entities.company;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "benefit", schema = "dbo")
public class BenefitEntity {


    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "companyBenefits")
    List<CompanyEntity> companies;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BenefitEntity)) return false;
        BenefitEntity that = (BenefitEntity) o;
        return getId() == that.getId() && getName().equals(that.getName()) && getDescription().equals(that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription());
    }
}
