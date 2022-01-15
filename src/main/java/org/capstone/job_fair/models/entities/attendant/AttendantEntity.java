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

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private CountryEntity country;

    @ManyToOne
    @JoinColumn(name = "residence_id")
    private ResidenceEntity residence;

    @ManyToOne
    @JoinColumn(name = "job_level_id")
    private JobLevelEntity currentJobLevel;


    @OneToMany(mappedBy = "attendant")
    private List<SkillEntity> skillEntities;

    @OneToMany(mappedBy = "attendant")
    private List<WorkHistoryEntity> workHistoryEntities;

    @OneToMany(mappedBy = "attendant")
    private List<EducationEntity> educationEntities;

    @OneToMany(mappedBy = "attendant")
    private List<CertificationEntity> certificationEntities;

    @OneToMany(mappedBy = "attendant")
    private List<ReferenceEntity> referenceEntities;

    @OneToMany(mappedBy = "attendant")
    private List<ActivityEntity> activityEntities;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AttendantEntity that = (AttendantEntity) o;

        return (!Objects.equals(accountId, that.accountId));
    }

    @Override
    public int hashCode() {
        int result = accountId != null ? accountId.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (dob != null ? dob.hashCode() : 0);
        result = 31 * result + (jobTitle != null ? jobTitle.hashCode() : 0);
        result = 31 * result + (yearOfExp != null ? yearOfExp.hashCode() : 0);
        result = 31 * result + (maritalStatus != null ? maritalStatus.hashCode() : 0);
        return result;
    }
}
