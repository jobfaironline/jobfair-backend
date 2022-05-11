package org.capstone.job_fair.models.entities.job_fair;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.capstone.job_fair.models.entities.attendant.application.ApplicationEntity;
import org.capstone.job_fair.models.enums.InterviewRequestChangeStatus;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "interview_request_change", schema = "dbo")
public class InterviewRequestChangeEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column(name = "message")
    private String message;
    @Column(name = "begin_time")
    private Long beginTime;
    @Column(name = "end_time")
    private Long endTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id")
    private ApplicationEntity application;
    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private InterviewRequestChangeStatus status;
    @Column(name = "create_time")
    private Long createTime;
    @Column(name = "evaluate_time")
    private Long evaluateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        InterviewRequestChangeEntity that = (InterviewRequestChangeEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
