package org.capstone.job_fair.models.dtos.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.dtos.account.AccountDTO;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyEmployeeDTO implements Serializable {
    private String accountId;
    private AccountDTO account;
    private CompanyDTO companyDTO;
}
