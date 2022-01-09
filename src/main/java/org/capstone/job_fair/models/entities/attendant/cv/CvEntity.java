package org.capstone.job_fair.models.entities.attendant.cv;

import lombok.*;
import org.capstone.job_fair.models.entities.attendant.CountryEntity;
import org.capstone.job_fair.models.entities.attendant.CurrentJobLevelEntity;
import org.capstone.job_fair.models.entities.attendant.GenderEntity;
import org.capstone.job_fair.models.entities.attendant.NationalityEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
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
    private CurrentJobLevelEntity currentJobLevel;

    @ManyToOne
    @JoinColumn(name = "gender_id")
    private GenderEntity gender;

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

        if (!Objects.equals(id, cvEntity.id)) return false;
        if (!Objects.equals(firstname, cvEntity.firstname)) return false;
        if (!Objects.equals(lastname, cvEntity.lastname)) return false;
        if (!Objects.equals(middlename, cvEntity.middlename)) return false;
        if (!Objects.equals(yearOfExp, cvEntity.yearOfExp)) return false;
        if (!Objects.equals(email, cvEntity.email)) return false;
        if (!Objects.equals(cellNumber, cvEntity.cellNumber)) return false;
        if (!Objects.equals(dob, cvEntity.dob)) return false;
        if (!Objects.equals(maritalStatus, cvEntity.maritalStatus))
            return false;
        if (!Objects.equals(address, cvEntity.address)) return false;
        if (!Objects.equals(summary, cvEntity.summary)) return false;
        if (!Objects.equals(createDate, cvEntity.createDate)) return false;
        if (!Objects.equals(nationality, cvEntity.nationality)) return false;
        if (!Objects.equals(country, cvEntity.country)) return false;
        if (!Objects.equals(currentJobLevel, cvEntity.currentJobLevel)) return false;
        if (!Objects.equals(gender, cvEntity.gender)) return false;
        if (!Objects.equals(skillEntities, cvEntity.skillEntities)) return false;
        if (!Objects.equals(workHistoryEntities, cvEntity.workHistoryEntities)) return false;
        if (!Objects.equals(educationEntities, cvEntity.educationEntities)) return false;
        if (!Objects.equals(certificationEntities, cvEntity.certificationEntities)) return false;
        if (!Objects.equals(referenceEntities, cvEntity.referenceEntities)) return false;
        if (!Objects.equals(activityEntities, cvEntity.activityEntities)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (middlename != null ? middlename.hashCode() : 0);
        result = 31 * result + (yearOfExp != null ? yearOfExp.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (cellNumber != null ? cellNumber.hashCode() : 0);
        result = 31 * result + (dob != null ? dob.hashCode() : 0);
        result = 31 * result + (maritalStatus != null ? maritalStatus.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (nationality != null ? nationality.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (currentJobLevel != null ? currentJobLevel.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        return result;
    }
}
