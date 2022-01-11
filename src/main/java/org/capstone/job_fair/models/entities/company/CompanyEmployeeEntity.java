package org.capstone.job_fair.models.entities.company;

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
@Table(name = "company_employee", schema = "dbo")
public class CompanyEmployeeEntity {
    @Id
    @Column(name = "account_id", nullable = false, length = 36)
    private String accountId;

    @OneToOne
    @JoinColumn(name = "account_id")
    private  AccountEntity account;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanyEmployeeEntity that = (CompanyEmployeeEntity) o;
        if (!Objects.equals(accountId, that.accountId)) return false;
        if (!Objects.equals(account, that.account)) return false;
        return Objects.equals(company, that.company);
    }

    @Override
    public int hashCode() {
        int result = accountId != null ? accountId.hashCode() : 0;
        result = 31 * result + (account != null ? account.hashCode() : 0);
        result = 31 * result + (company != null ? company.hashCode() : 0);
        return result;
    }
}
