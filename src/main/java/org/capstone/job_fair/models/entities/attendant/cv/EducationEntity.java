package org.capstone.job_fair.models.entities.attendant.cv;

import lombok.*;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.capstone.job_fair.models.entities.attendant.QualificationEntity;
import org.capstone.job_fair.models.enums.Qualification;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "education", schema = "dbo")
public class EducationEntity {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "subject", nullable = false, length = 100)
    private String subject;

    @Column(name = "school", length = 100)
    private String school;

    @Column(name = "from_date")
    private Long fromDate;

    @Column(name = "to_date")
    private Long toDate;

    @Column(name = "achievement")
    private String achievement;

    @ManyToOne
    @JoinColumn(name = "attendant_id")
    private AttendantEntity attendant;

    @Column(name = "qualification_id", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Qualification qualification;

}
