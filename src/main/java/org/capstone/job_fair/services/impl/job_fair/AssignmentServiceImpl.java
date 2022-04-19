package org.capstone.job_fair.services.impl.job_fair;

import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.dtos.job_fair.AssignmentDTO;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.capstone.job_fair.models.entities.company.JobFairBoothEntity;
import org.capstone.job_fair.models.entities.job_fair.AssignmentEntity;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.enums.AssignmentType;
import org.capstone.job_fair.models.statuses.JobFairPlanStatus;
import org.capstone.job_fair.repositories.company.CompanyEmployeeRepository;
import org.capstone.job_fair.repositories.company.JobFairBoothRepository;
import org.capstone.job_fair.repositories.job_fair.AssignmentRepository;
import org.capstone.job_fair.repositories.job_fair.JobFairRepository;
import org.capstone.job_fair.services.interfaces.job_fair.AssignmentService;
import org.capstone.job_fair.services.mappers.company.CompanyEmployeeMapper;
import org.capstone.job_fair.services.mappers.job_fair.AssignmentMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AssignmentServiceImpl implements AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private AssignmentMapper assignmentMapper;

    @Autowired
    private CompanyEmployeeRepository companyEmployeeRepository;

    @Autowired
    private JobFairBoothRepository jobFairBoothRepository;

    @Autowired
    private JobFairRepository jobFairRepository;

    @Autowired
    private CompanyEmployeeMapper companyEmployeeMapper;


    private AssignmentDTO updateAssigment(AssignmentEntity entity, String companyId) {
        CompanyEmployeeEntity companyEmployee = entity.getCompanyEmployee();
        if (!companyEmployee.getCompany().getId().equals(companyId)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Assignment.NOT_FOUND));
        }
        assignmentRepository.save(entity);
        return assignmentMapper.toDTO(entity);
    }

    private AssignmentDTO createAssignment(String employeeId, String jobFairBoothId, AssignmentType type, String companyId) {
        Optional<CompanyEmployeeEntity> employeeOpt = companyEmployeeRepository.findByAccountId(employeeId);
        if (!employeeOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyEmployee.EMPLOYEE_NOT_FOUND));
        }
        CompanyEmployeeEntity employee = employeeOpt.get();
        if (!employee.getCompany().getId().equals(companyId)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Assignment.NOT_FOUND));
        }
        Optional<JobFairBoothEntity> jobFairBoothOpt = jobFairBoothRepository.findById(jobFairBoothId);
        if (!jobFairBoothOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFairBooth.NOT_FOUND));
        }
        JobFairBoothEntity jobFairBooth = jobFairBoothOpt.get();
        if (!jobFairBooth.getJobFair().getCompany().getId().equals(companyId)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFairBooth.NOT_FOUND));
        }

        AssignmentEntity entity = new AssignmentEntity();
        entity.setCompanyEmployee(employee);
        entity.setJobFairBooth(jobFairBooth);
        entity.setType(type);
        assignmentRepository.save(entity);
        return assignmentMapper.toDTO(entity);
    }

    @Override
    @Transactional
    public AssignmentDTO assignEmployee(String employeeId, String jobFairBoothId, AssignmentType type, String companyId) {
        Optional<AssignmentEntity> entityOpt = assignmentRepository.findByCompanyEmployeeAccountIdAndJobFairBoothId(employeeId, jobFairBoothId);
        if (entityOpt.isPresent()) {
            AssignmentEntity entity = entityOpt.get();
            entity.setType(type);
            return updateAssigment(entity, companyId);
        }
        return createAssignment(employeeId, jobFairBoothId, type, companyId);
    }

    @Override
    @Transactional
    public AssignmentDTO unAssignEmployee(String employeeId, String jobFairBoothId, String companyId) {
        Optional<AssignmentEntity> entityOpt = assignmentRepository.findByCompanyEmployeeAccountIdAndJobFairBoothId(employeeId, jobFairBoothId);
        if (!entityOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Assignment.NOT_FOUND));
        }
        AssignmentEntity entity = entityOpt.get();
        if (!entity.getCompanyEmployee().getCompany().getId().equals(companyId)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Assignment.NOT_FOUND));
        }
        assignmentRepository.delete(entity);
        return assignmentMapper.toDTO(entity);
    }

    @Override
    public List<AssignmentDTO> getAssignmentByJobFairId(String jobFairId, String companyId) {
        return assignmentRepository.findByJobFairBoothJobFairIdAndJobFairBoothJobFairCompanyId(jobFairId, companyId).stream().map(assignmentMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<AssignmentDTO> getAssigmentByJobFairBoothId(String jobFairBoothId, String companyId) {
        return assignmentRepository.findByJobFairBoothIdAndJobFairBoothJobFairCompanyId(jobFairBoothId, companyId).stream().map(assignmentMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<CompanyEmployeeDTO> getAvailableCompanyByJobFairId(String jobFairId, String companyId) {
        Optional<JobFairEntity> jobFairOpt = jobFairRepository.findByIdAndCompanyId(jobFairId, companyId);
        if (!jobFairOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.JOB_FAIR_NOT_FOUND));
        }
        List<AssignmentEntity> assignmentsInJobFair = assignmentRepository.findByJobFairBoothJobFairIdAndJobFairBoothJobFairCompanyId(jobFairId, companyId);
        List<CompanyEmployeeEntity> companyEmployees = companyEmployeeRepository.findAllByCompanyId(companyId);

        companyEmployees = companyEmployees.stream().filter(companyEmployee -> {
            boolean result = assignmentsInJobFair.stream().anyMatch(assignment -> assignment.getCompanyEmployee().getAccountId().equals(companyEmployee.getAccountId()));
            return !result;
        }).collect(Collectors.toList());
        return companyEmployees.stream().map(companyEmployeeMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Integer getCountAssignedBoothByJobFair(String jobFairId) {
        return assignmentRepository.countDistinctJobFairBoothByJobFairBoothJobFairId(jobFairId);
    }

    @Override
    public Integer getCountAssignedEmployeeByJobFair(String jobFairId) {
        return assignmentRepository.countByJobFairBoothJobFairId(jobFairId);
    }

    @Override
    public Page<AssignmentDTO> getAssignmentByEmployeeId(String employeeId, Pageable pageable) {
        return assignmentRepository.findByCompanyEmployeeAccountIdAndJobFairBoothJobFairStatus(employeeId, JobFairPlanStatus.PUBLISH, pageable).map(assignmentMapper::toDTO);
    }

    @Override
    public Optional<AssignmentDTO> getAssignmentById(String id) {
        return assignmentRepository.findById(id).map(assignmentMapper::toDTO);
    }
}
