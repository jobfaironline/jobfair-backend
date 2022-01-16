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
@Table(name = "language", schema = "dbo")
public class LanguageEntity {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id", length = 3)
    private String id;
    @Basic
    @Column(name = "name")
    private String name;

}
