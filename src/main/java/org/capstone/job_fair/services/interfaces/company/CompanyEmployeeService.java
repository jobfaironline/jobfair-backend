package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;

import java.util.List;

public interface CompanyEmployeeService {
    void createNewCompanyManagerAccount(CompanyEmployeeDTO dto);

    void createNewCompanyEmployeeAccount(CompanyEmployeeDTO dto, String companyId);

    void updateProfile(CompanyEmployeeDTO dto);

    List<CompanyEmployeeDTO> getAllCompanyEmployees (String id);

    Boolean deleteEmployee(String id);

    public void updateEmployeeStatus(String email);
}
