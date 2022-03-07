package org.capstone.job_fair.models.entities.job_fair;

import lombok.*;
import org.capstone.job_fair.models.statuses.JobFairAttendantStatus;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attendant_job_fair_status", schema = "dbo")
public class AttendantJobFairStatusEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;
    @Column(name = "attendantId", length = 36)
    private String attendantId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobFairId")
    private JobFairEntity jobFair;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private JobFairAttendantStatus status;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AttendantJobFairStatusEntity that = (AttendantJobFairStatusEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
