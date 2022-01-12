package org.capstone.job_fair.models.entities.attendant;

import lombok.*;
import org.capstone.job_fair.models.entities.account.AccountEntity;

import javax.persistence.*;
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

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "account_id")
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
    @JoinColumn(name = "job_level_id")
    private JobLevelEntity currentJobLevel;




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
