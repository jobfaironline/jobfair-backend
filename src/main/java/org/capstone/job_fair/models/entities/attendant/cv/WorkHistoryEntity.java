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
@Table(name = "work_history", schema = "dbo")
public class WorkHistoryEntity {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;
    @Basic
    @Column(name = "position", length = 100)
    private String position;
    @Basic
    @Column(name = "company", length = 100)
    private String company;
    @Basic
    @Column(name = "from_date")
    private Long fromDate;
    @Basic
    @Column(name = "to_date")
    private Long toDate;
    @Basic
    @Column(name = "is_current_job")
    private Boolean isCurrentJob;
    @Basic
    @Column(name = "description", nullable = true, length = 5000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendant_id")
    private AttendantEntity attendant;


}
