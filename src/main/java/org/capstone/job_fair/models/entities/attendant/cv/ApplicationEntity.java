package org.capstone.job_fair.models.entities.attendant.cv;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.company.job.RegistrationJobPositionEntity;
import org.capstone.job_fair.models.enums.ApplicationStatus;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "application", schema = "dbo")
public class ApplicationEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "summary")
    private String summary;

    @Column(name = "create_date")
    private Long createDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private ApplicationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cv_id")
    private CvEntity cv;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_job_position_id")
    private RegistrationJobPositionEntity registrationJobPosition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorizer_id")
    private AccountEntity authorizer;

    @Column(name = "evaluate_message")
    private String evaluateMessage;

    @Column(name = "evaluate_date")
    private Long evaluateDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ApplicationEntity that = (ApplicationEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "summary = " + summary + ", " +
                "createDate = " + createDate + ", " +
                "status = " + status + ")";
    }
}
