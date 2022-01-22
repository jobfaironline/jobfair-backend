package org.capstone.job_fair.models.entities.attendant.cv;

import lombok.*;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "certification", schema = "dbo")
public class CertificationEntity {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "institution")
    private String institution;

    @Column(name = "year")
    private Integer year;

    @Column(name = "certification_link")
    private String certificationLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendant_id")
    private AttendantEntity attendant;

}
