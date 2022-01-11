package org.capstone.job_fair.models.entities.attendant.cv;

import lombok.*;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.attendant.CountryEntity;
import org.capstone.job_fair.models.entities.attendant.JobLevelEntity;
import org.capstone.job_fair.models.entities.account.GenderEntity;
import org.capstone.job_fair.models.entities.attendant.NationalityEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cv", schema = "dbo")
public class CvEntity {
    @Id
    @Column(name = "id")
    private String id;
    @Basic
    @Column(name = "firstname")
    private String firstname;
    @Basic
    @Column(name = "lastname")
    private String lastname;
    @Basic
    @Column(name = "middlename")
    private String middlename;
    @Basic
    @Column(name = "year_of_exp")
    private Double yearOfExp;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "phone")
    private String cellNumber;
    @Basic
    @Column(name = "dob")
    private Long dob;
    @Basic
    @Column(name = "marital_status")
    private Boolean maritalStatus;
    @Basic
    @Column(name = "address")
    private String address;
    @Basic
    @Column(name = "summary")
    private String summary;
    @Basic
    @Column(name = "create_date")
    private Long createDate;

    @ManyToOne
    @JoinColumn(name = "nationality_id")
    private NationalityEntity nationality;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private CountryEntity country;

    @ManyToOne
    @JoinColumn(name = "current_job_level_id")
    private JobLevelEntity currentJobLevel;

    @ManyToOne
    @JoinColumn(name = "gender_id")
    private GenderEntity gender;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @OneToMany(mappedBy = "cv")
    private List<SkillEntity> skillEntities;

    @OneToMany(mappedBy = "cv")
    private List<WorkHistoryEntity> workHistoryEntities;

    @OneToMany(mappedBy = "cv")
    private List<EducationEntity> educationEntities;

    @OneToMany(mappedBy = "cv")
    private List<CertificationEntity> certificationEntities;

    @OneToMany(mappedBy = "cv")
    private List<ReferenceEntity> referenceEntities;

    @OneToMany(mappedBy = "cv")
    private List<ActivityEntity> activityEntities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CvEntity cvEntity = (CvEntity) o;

        return id.equals(cvEntity.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
