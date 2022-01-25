package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;

import java.util.List;

public interface CompanyEmployeeService {
    CompanyEmployeeDTO createNewCompanyManagerAccount(CompanyEmployeeDTO dto);

    void createNewCompanyEmployeeAccount(CompanyEmployeeDTO dto);

    void updateProfile(CompanyEmployeeDTO dto);

    List<CompanyEmployeeDTO> getAllCompanyEmployees (String id);

    Boolean deleteEmployee(String id);

    public void updateEmployeeStatus(String email);

    void promoteEmployee(String employeeId, String managerId);

    boolean isSameCompany(String employeeId, String managerId);

    CompanyEmployeeDTO getById(String id);
}
