package org.capstone.job_fair.controllers.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyEmployeeResponse {
    private String accountId;
    private AccountDTO account;
    private String companyId;
    private String department;
    private String employeeId;
}
