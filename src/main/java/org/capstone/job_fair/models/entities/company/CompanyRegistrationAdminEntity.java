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
public class CompanyRegistrationAdminEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "createDate")
    private Long createDate;
    @Column(name = "description")
    private String description;
    @Column(name = "jobFairId")
    private String jobFairId;
    @Column(name = "companyId")
    private String companyId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private CompanyRegistrationStatus status;

    @Column(name = "cancelReason")
    private String cancelReason;

    @Column(name = "adminMessage")
    private String adminMessage;

    @Column(name = "authorizerId")
    private String authorizerId;

    @Column(name = "creatorId")
    private String creatorId;

    @Column(name = "jobFairName", insertable = false, updatable = false)
    private String companyName;

    @Column(name = "companyName", insertable = false, updatable = false)
    private String jobfairName;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "company_registration_id", referencedColumnName = "id")
    private List<RegistrationJobPositionEntity> registrationJobPositions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CompanyRegistrationAdminEntity that = (CompanyRegistrationAdminEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
