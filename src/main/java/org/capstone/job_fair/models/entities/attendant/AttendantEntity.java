package org.capstone.job_fair.models.entities.attendant;

import lombok.*;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.attendant.cv.*;
import org.capstone.job_fair.models.enums.JobLevel;
import org.capstone.job_fair.models.enums.Marital;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attendant", schema = "dbo")
public class AttendantEntity {
    @Id
    @Column(name = "account_id", nullable = false, length = 36)
    private String accountId;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "address", length = 1000)
    private String address;

    @Column(name = "dob")
    private Long dob;

    @Column(name = "job_title", length = 100)
    private String jobTitle;

    @Column(name = "year_of_exp")
    private Double yearOfExp;

    @Column(name = "marital_status")
    @Enumerated(EnumType.ORDINAL)
    private Marital maritalStatus;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private CountryEntity country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "residence_id")
    private ResidenceEntity residence;

    @Column(name = "job_level_id")
    @Enumerated(EnumType.ORDINAL)
    private JobLevel currentJobLevel;


    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "attendant_id", referencedColumnName = "account_id")
    private List<SkillEntity> skillEntities;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "attendant_id", referencedColumnName = "account_id")
    private List<WorkHistoryEntity> workHistoryEntities;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "attendant_id", referencedColumnName = "account_id")
    private List<EducationEntity> educationEntities;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "attendant_id", referencedColumnName = "account_id")
    private List<CertificationEntity> certificationEntities;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "attendant_id", referencedColumnName = "account_id")
    private List<ReferenceEntity> referenceEntities;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "attendant_id", referencedColumnName = "account_id")
    private List<ActivityEntity> activityEntities;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AttendantEntity entity = (AttendantEntity) o;
        return accountId != null && Objects.equals(accountId, entity.accountId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
