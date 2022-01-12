package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;

public interface CompanyEmployeeService {
    void createNewCompanyManagerAccount(CompanyEmployeeDTO dto);
}
