package org.capstone.job_fair.models.entities.attendant;

import org.capstone.job_fair.models.entities.AccountEntity;

import javax.persistence.*;

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




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AttendantEntity that = (AttendantEntity) o;

        if (accountId != null ? !accountId.equals(that.accountId) : that.accountId != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (dob != null ? !dob.equals(that.dob) : that.dob != null) return false;
        if (jobTitle != null ? !jobTitle.equals(that.jobTitle) : that.jobTitle != null) return false;
        if (yearOfExp != null ? !yearOfExp.equals(that.yearOfExp) : that.yearOfExp != null) return false;
        if (maritalStatus != null ? !maritalStatus.equals(that.maritalStatus) : that.maritalStatus != null)
            return false;

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
