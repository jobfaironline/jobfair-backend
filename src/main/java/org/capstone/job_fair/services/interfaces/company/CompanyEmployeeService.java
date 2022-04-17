package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;

import java.util.List;
import java.util.Optional;

public interface CompanyEmployeeService {
    CompanyEmployeeDTO createNewCompanyManagerAccount(CompanyEmployeeDTO dto);

    void createNewCompanyEmployeeAccount(CompanyEmployeeDTO dto);

    void updateProfile(CompanyEmployeeDTO dto);

    List<CompanyEmployeeDTO> getAllCompanyEmployees(String id);

    Boolean deleteEmployee(String id);

    void updateEmployeeStatus(String email);

    void promoteEmployee(String employeeId, String managerId);

    Optional<CompanyEmployeeDTO> getCompanyEmployeeByAccountId(String accountID);

    CompanyEmployeeDTO getCompanyEmployeeByAccountId(String employeeID, String companyID);

    Integer getCompanyEmployeeCount(String companyId);
}
