package org.capstone.job_fair.models.entities.company;

import lombok.*;
import org.capstone.job_fair.models.entities.company.job.RegistrationJobPositionEntity;
import org.capstone.job_fair.models.statuses.CompanyRegistrationStatus;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "company_registration", schema = "dbo")
public class CompanyRegistrationEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "create_date")
    private Long createDate;
    @Column(name = "description")
    private String description;
    @Column(name = "job_fair_id")
    private String jobFairId;
    @Column(name = "company_id")
    private String companyId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private CompanyRegistrationStatus status;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @Column(name = "admin_message")
    private String adminMessage;

    @Column(name = "authorizer_id")
    private String authorizerId;

    @Column(name = "creator_id")
    private String creatorId;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "company_registration_id", referencedColumnName = "id")
    private List<RegistrationJobPositionEntity> registrationJobPositions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CompanyRegistrationEntity that = (CompanyRegistrationEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
