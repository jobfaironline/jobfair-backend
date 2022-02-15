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
    @Column(name = "company_register_start_time")
    private long companyRegisterStartTime;
    @Column(name = "company_register_end_time")
    private long companyRegisterEndTime;
    @Column(name = "company_buy_booth_start_time")
    private long companyBuyBoothStartTime;
    @Column(name = "company_buy_booth_end_time")
    private long companyBuyBoothEndTime;
    @Column(name = "attendant_register_start_time")
    private long attendantRegisterStartTime;
    @Column(name = "start_time")
    private Long startTime;
    @Column(name = "end_time")
    private long endTime;
    @Column(name = "description")
    private String description;
    @Column(name = "layout_id")
    private String layoutId;
    @Column(name = "status")
    private JobFairStatus status;
    @Column(name = "creator_id")
    private String creatorId;
    @Column(name = "authorizer_id")
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
        return getClass().hashCode();
    }

}
