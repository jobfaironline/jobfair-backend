package org.capstone.job_fair.models.entities.company.job;

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
@Table(name = "job_type", schema = "dbo")
public class JobTypeEntity {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "name", length = 100)
    private String name;


}
