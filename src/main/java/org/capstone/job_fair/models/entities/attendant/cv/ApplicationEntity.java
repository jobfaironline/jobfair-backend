package org.capstone.job_fair.models.entities.attendant.cv;

import lombok.*;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.capstone.job_fair.models.entities.attendant.CountryEntity;
import org.capstone.job_fair.models.entities.attendant.JobLevelEntity;
import org.capstone.job_fair.models.entities.account.GenderEntity;
import org.capstone.job_fair.models.enums.Application;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "application", schema = "dbo")
public class ApplicationEntity {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "summary")
    private String summary;

    @Column(name = "create_date")
    private Long createDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Application status;

    @ManyToOne
    @JoinColumn(name = "attendant_id")
    private AttendantEntity attendant;

    @ManyToOne()
    @JoinColumn(name = "job_position_id", nullable = false)
    private JobLevelEntity jobLevel;

}
