package org.capstone.job_fair.services.interfaces.job_fair.booth;

import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.AssignmentDTO;
import org.capstone.job_fair.models.enums.AssignmentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AssignmentService {
    AssignmentDTO assignEmployee(String employeeId, String jobFairBoothId, AssignmentType type, String companyId, Long beginTime, Long endTime);

    AssignmentDTO unAssignEmployee(String assignmentId);

    AssignmentDTO updateAssignment(String assignmentId, long beginTime, long endTime);

    List<AssignmentDTO> getAssignmentByJobFairId(String jobFairId, String companyId);

    List<AssignmentDTO> getAssigmentByJobFairBoothId(String jobFairBoothId, String companyId);

    List<CompanyEmployeeDTO> getAvailableCompanyByJobFairId(String jobFairId, String companyId);

    Integer getCountAssignedBoothByJobFair(String jobFairId);

    Integer getCountAssignedEmployeeByJobFair(String jobFairId);

    Page<AssignmentDTO> getAssignmentByEmployeeId(String employeeId, Pageable pageable);

    Optional<AssignmentDTO> getAssignmentById(String id);


}
