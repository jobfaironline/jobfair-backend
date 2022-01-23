package org.capstone.job_fair.models.entities.attendant;

import lombok.*;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.attendant.cv.*;
import org.capstone.job_fair.models.enums.Marital;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "attendant", schema = "dbo")
public class AttendantEntity {
    @EqualsAndHashCode.Include
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

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private CountryEntity country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "residence_id")
    private ResidenceEntity residence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_level_id")
    private JobLevelEntity currentJobLevel;


    @OneToMany(mappedBy = "attendant", fetch = FetchType.LAZY)
    private List<SkillEntity> skillEntities;

    @OneToMany(mappedBy = "attendant", fetch = FetchType.LAZY)
    private List<WorkHistoryEntity> workHistoryEntities;

    @OneToMany(mappedBy = "attendant", fetch = FetchType.LAZY)
    private List<EducationEntity> educationEntities;

    @OneToMany(mappedBy = "attendant", fetch = FetchType.LAZY)
    private List<CertificationEntity> certificationEntities;

    @OneToMany(mappedBy = "attendant", fetch = FetchType.LAZY)
    private List<ReferenceEntity> referenceEntities;

    @OneToMany(mappedBy = "attendant", fetch = FetchType.LAZY)
    private List<ActivityEntity> activityEntities;

}
