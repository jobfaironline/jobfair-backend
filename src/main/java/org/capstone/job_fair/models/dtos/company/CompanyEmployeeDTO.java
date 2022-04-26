package org.capstone.job_fair.models.dtos.company;

import lombok.*;
import org.capstone.job_fair.models.dtos.account.AccountDTO;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CompanyEmployeeDTO implements Serializable {
    @EqualsAndHashCode.Include
    private String accountId;
    private AccountDTO account;
    private CompanyDTO companyDTO;
    private String department;
    private String employeeId;
}
