package org.capstone.job_fair.services.interfaces.job_fair.booth;

import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.AssignmentDTO;
import org.capstone.job_fair.models.dtos.util.ParseFileResult;
import org.capstone.job_fair.models.enums.AssignmentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface AssignmentService {
    AssignmentDTO assignEmployee(String assignerId, String employeeId, String jobFairBoothId, AssignmentType type, String companyId, Long beginTime, Long endTime);

    AssignmentDTO unAssignEmployee(String assignmentId);

    AssignmentDTO updateAssignment(String assignmentId, long beginTime, long endTime, AssignmentType type);

    List<AssignmentDTO> getAssignmentByJobFairId(String jobFairId, String companyId);

    List<AssignmentDTO> getAssigmentByJobFairBoothId(String jobFairBoothId, String companyId);

    List<CompanyEmployeeDTO> getAvailableCompanyByJobFairId(String jobFairId, String companyId);

    Integer getCountAssignedBoothByJobFair(String jobFairId);

    Integer getCountAssignedEmployeeByJobFair(String jobFairId);

    Page<AssignmentDTO> getAssignmentByEmployeeIdAndType(String employeeId, AssignmentType type, Pageable pageable);

    Optional<AssignmentDTO> getAssignmentById(String id);

    ParseFileResult<AssignmentDTO> createNewAssignmentsFromFile(MultipartFile file, String jobFairId, String companyId, String assignerId);

    List<CompanyEmployeeDTO> getAvailableInterviewer(String jobFairBoothId);
}
