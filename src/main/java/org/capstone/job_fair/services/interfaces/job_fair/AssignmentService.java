package org.capstone.job_fair.services.interfaces.job_fair;

import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.dtos.job_fair.AssignmentDTO;
import org.capstone.job_fair.models.enums.AssignmentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AssignmentService {
    AssignmentDTO assignEmployee(String employeeId, String jobFairBoothId, AssignmentType type, String companyId);

    AssignmentDTO unAssignEmployee(String employeeId, String jobFairBoothId, String companyId);

    List<AssignmentDTO> getAssignmentByJobFairId(String jobFairId, String companyId);

    List<AssignmentDTO> getAssigmentByJobFairBoothId(String jobFairBoothId, String companyId);

    List<CompanyEmployeeDTO> getAvailableCompanyByJobFairId(String jobFairId, String companyId);

    Integer getCountAssignedBoothByJobFair(String jobFairId);

    Integer getCountAssignedEmployeeByJobFair(String jobFairId);

    Page<AssignmentDTO> getAssignmentByEmployeeId(String employeeId, Pageable pageable);


}
