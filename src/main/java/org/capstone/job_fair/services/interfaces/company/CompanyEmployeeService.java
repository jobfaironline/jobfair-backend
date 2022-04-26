package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CompanyEmployeeService {
    CompanyEmployeeDTO createNewCompanyManagerAccount(CompanyEmployeeDTO dto);

    void createNewCompanyEmployeeAccount(CompanyEmployeeDTO dto);

    void updateProfile(CompanyEmployeeDTO dto);

    Page<CompanyEmployeeDTO> getAllCompanyEmployees(String companyId, String searchContent, int pageSize, int offset, String sortBy, Sort.Direction direction);

    Boolean deleteEmployee(String id);

    void updateEmployeeStatus(String email);

    void promoteEmployee(String employeeId, String managerId);

    Optional<CompanyEmployeeDTO> getCompanyEmployeeByAccountId(String accountID);

    CompanyEmployeeDTO getCompanyEmployeeByAccountId(String employeeID, String companyID);

    Integer getCompanyEmployeeCount(String companyId);

    List<CompanyEmployeeDTO> createNewCompanyEmployeesFromCSVFile(MultipartFile file, String companyId);
}
