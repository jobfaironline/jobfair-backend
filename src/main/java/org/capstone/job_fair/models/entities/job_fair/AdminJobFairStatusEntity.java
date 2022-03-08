package org.capstone.job_fair.models.entities.job_fair;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.capstone.job_fair.models.statuses.JobFairAdminStatus;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admin_job_fair_status", schema = "dbo")
public class AdminJobFairStatusEntity {
    @Id
    @Column(name = "jobFairId", nullable = false, length = 36)
    private String jobFairId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobFairId")
    @MapsId
    private JobFairEntity jobFair;
    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private JobFairAdminStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AdminJobFairStatusEntity entity = (AdminJobFairStatusEntity) o;
        return jobFair != null && Objects.equals(jobFairId, entity.getJobFairId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
