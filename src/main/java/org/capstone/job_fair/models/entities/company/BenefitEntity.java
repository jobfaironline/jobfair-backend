package org.capstone.job_fair.models.entities.company;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "benefit", schema = "dbo")
public class BenefitEntity {

    public BenefitEntity(int id) {
        this.id = id;
    }

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;



}
