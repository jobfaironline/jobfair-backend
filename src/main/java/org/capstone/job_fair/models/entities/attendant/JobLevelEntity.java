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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "job_level", schema = "dbo")
public class JobLevelEntity {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 100)
    private String name;


}
