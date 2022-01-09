package org.capstone.job_fair.models.entities.attendant;

import org.capstone.job_fair.models.entities.AccountEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "attendant", schema = "dbo")
public class AttendantEntity {
    @Id
    @Column(name = "account_id", nullable = false, length = 36)
    private String accountId;

    @OneToOne
    @MapsId
    private AccountEntity account;

    @Basic
    @Column(name = "title", nullable = true, length = 100)
    private String title;
    @Basic
    @Column(name = "address", nullable = true, length = 1000)
    private String address;
    @Basic
    @Column(name = "dob", nullable = true)
    private Long dob;
    @Basic
    @Column(name = "job_title", nullable = true, length = 100)
    private String jobTitle;
    @Basic
    @Column(name = "year_of_exp", nullable = true)
    private Double yearOfExp;
    @Basic
    @Column(name = "marital_status", nullable = true)
    private Boolean maritalStatus;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private CountryEntity country;

    @ManyToOne
    @JoinColumn(name = "residence_id")
    private ResidenceEntity residence;

    @ManyToOne
    @JoinColumn(name = "current_job_level_id")
    private CurrentJobLevelEntity currentJobLevel;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AttendantEntity that = (AttendantEntity) o;

        if (!Objects.equals(accountId, that.accountId)) return false;
        if (!Objects.equals(title, that.title)) return false;
        if (!Objects.equals(address, that.address)) return false;
        if (!Objects.equals(dob, that.dob)) return false;
        if (!Objects.equals(jobTitle, that.jobTitle)) return false;
        if (!Objects.equals(yearOfExp, that.yearOfExp)) return false;
        if (!Objects.equals(maritalStatus, that.maritalStatus)) return false;
        if (!Objects.equals(country, that.country)) return false;
        if (!Objects.equals(residence, that.residence)) return false;
        if (!Objects.equals(currentJobLevel, that.currentJobLevel)) return false;

        return true;
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
