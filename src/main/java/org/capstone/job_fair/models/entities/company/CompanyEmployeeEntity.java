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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "company_employee", schema = "dbo")
public class CompanyEmployeeEntity {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "account_id", nullable = false, length = 36)
    private String accountId;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "account_id")
    private  AccountEntity account;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

}
