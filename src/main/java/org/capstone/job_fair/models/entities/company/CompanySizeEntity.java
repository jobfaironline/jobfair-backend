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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "company_size", schema = "dbo")
public class CompanySizeEntity {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

}
