package org.capstone.job_fair.models.entities.job_fair;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.JobFairBoothEntity;
import org.capstone.job_fair.models.statuses.JobFairPlanStatus;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "job_fair", schema = "dbo")
public class JobFairEntity {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column(name = "create_time")
    private Long createTime;
    @Column(name = "decorate_start_time")
    private Long decorateStartTime;
    @Column(name = "decorate_end_time")
    private Long decorateEndTime;
    @Column(name = "public_start_time")
    private Long publicStartTime;
    @Column(name = "public_end_time")
    private Long publicEndTime;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "description", length = 500)
    private String description;
    @Column(name = "target_attendant", length = 100)
    private String targetAttendant;
    @Column(name = "thumbnail_url", length = 2048)
    private String thumbnailUrl;
    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private JobFairPlanStatus status;
    @Column(name = "cancel_reason", length = 500)
    private String cancelReason;
    @Column(name = "host_name", length = 100)
    private String hostName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_fair_id", referencedColumnName = "id")
    private List<JobFairBoothEntity> jobFairBoothList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "job_fair_id", referencedColumnName = "id")
    private List<ShiftEntity> shifts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        JobFairEntity that = (JobFairEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
