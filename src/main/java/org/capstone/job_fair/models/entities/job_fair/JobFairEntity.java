package org.capstone.job_fair.models.entities.job_fair;

import lombok.*;
import org.capstone.job_fair.models.statuses.JobFairStatus;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "job_fair", schema = "dbo")
public class JobFairEntity {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Basic
    @Column(name = "company_register_start_time")
    private long companyRegisterStartTime;
    @Basic
    @Column(name = "company_register_end_time")
    private long companyRegisterEndTime;
    @Basic
    @Column(name = "company_buy_booth_start_time")
    private long companyBuyBoothStartTime;
    @Basic
    @Column(name = "company_buy_booth_end_time")
    private long companyBuyBoothEndTime;
    @Basic
    @Column(name = "attendant_register_start_time")
    private long attendantRegisterStartTime;
    @Basic
    @Column(name = "start_time")
    private Long startTime;
    @Basic
    @Column(name = "end_time")
    private long endTime;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "layout_id", nullable = true)
    private String layoutId;
    @Basic
    @Column(name = "status")
    private JobFairStatus status;
    @Basic
    @Column(name = "creator_id")
    private String creatorId;
    @Basic
    @Column(name = "authorizer_id", nullable = true)
    private String authorizerId;
    @Column(name = "reject_reason")
    private String rejectReason;
    @Column(name = "cancel_reason")
    private String cancelReason;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        JobFairEntity that = (JobFairEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
