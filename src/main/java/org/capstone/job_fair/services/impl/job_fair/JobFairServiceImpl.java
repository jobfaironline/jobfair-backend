package org.capstone.job_fair.services.impl.job_fair;

import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;
import org.capstone.job_fair.models.dtos.job_fair.JobFairProgressDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.JobFairBoothLayoutDTO;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.AssignmentEntity;
import org.capstone.job_fair.models.enums.AssignmentType;
import org.capstone.job_fair.models.statuses.JobFairPlanStatus;
import org.capstone.job_fair.repositories.job_fair.JobFairRepository;
import org.capstone.job_fair.repositories.job_fair.ShiftRepository;
import org.capstone.job_fair.repositories.job_fair.job_fair_booth.AssignmentRepository;
import org.capstone.job_fair.services.interfaces.job_fair.JobFairService;
import org.capstone.job_fair.services.interfaces.job_fair.booth.AssignmentService;
import org.capstone.job_fair.services.interfaces.job_fair.booth.JobFairBoothLayoutService;
import org.capstone.job_fair.services.mappers.job_fair.JobFairMapper;
import org.capstone.job_fair.utils.AwsUtil;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class JobFairServiceImpl implements JobFairService {

    @Autowired
    private JobFairRepository jobFairRepository;

    @Autowired
    private JobFairMapper jobFairMapper;

    @Autowired
    private Validator validator;

    @Autowired
    private AwsUtil awsUtil;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private JobFairBoothLayoutService jobFairBoothLayoutService;

    @Override
    public Optional<JobFairDTO> getById(String id) {
        return jobFairRepository.findById(id).map(jobFairMapper::toDTO);
    }

    private void validateJobFairTime(JobFairDTO dto) {
        if (dto.getDecorateEndTime() != null && dto.getDecorateStartTime() != null && dto.getDecorateEndTime() < dto.getDecorateStartTime() + DataConstraint.JobFair.MINIMUM_DECORATE_TIME) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.INVALID_DECORATE_TIME));
        }
        if (dto.getDecorateStartTime() != null && dto.getDecorateEndTime() != null && dto.getDecorateEndTime() < dto.getDecorateStartTime() + DataConstraint.JobFair.MINIMUM_PUBLIC_TIME) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.INVALID_DECORATE_TIME));
        }
        if (dto.getPublicStartTime() != null && dto.getDecorateEndTime() != null && dto.getPublicStartTime() < dto.getDecorateEndTime() + DataConstraint.JobFair.MINIMUM_BUFFER_TIME) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.INVALID_BUFFER_TIME));
        }
    }

    @Override
    @Transactional
    public JobFairDTO createNewJobFair(final JobFairDTO dto) {
        long now = new Date().getTime();
        dto.setCreateTime(now);
        dto.setStatus(JobFairPlanStatus.DRAFT);
        validateJobFairTime(dto);
        JobFairEntity entity = jobFairMapper.toEntity(dto);
        entity = jobFairRepository.save(entity);
        return jobFairMapper.toDTO(entity);
    }

    @Override
    @Transactional
    public JobFairDTO updateJobFair(JobFairDTO dto, String companyID) {
        Optional<JobFairEntity> opt = jobFairRepository.findByIdAndCompanyId(dto.getId(), companyID);
        if (!opt.isPresent()) {
            throw new IllegalArgumentException(MessageConstant.JobFair.JOB_FAIR_NOT_FOUND);
        }
        JobFairEntity entity = opt.get();
        if (dto.getShifts() != null && !dto.getShifts().isEmpty()) {
            shiftRepository.deleteAll(entity.getShifts());
            entity.setShifts(new ArrayList<>());
        }
        jobFairMapper.updateFromDTO(entity, dto);
        shiftRepository.saveAll(entity.getShifts());
        entity = jobFairRepository.save(entity);
        return jobFairMapper.toDTO(entity);
    }

    @Override
    @Transactional
    public JobFairDTO deleteJobFair(String jobFairId, String companyID) {
        Optional<JobFairEntity> opt = jobFairRepository.findByIdAndCompanyId(jobFairId, companyID);
        if (!opt.isPresent()) {
            throw new IllegalArgumentException(MessageConstant.JobFair.JOB_FAIR_NOT_FOUND);
        }
        JobFairEntity entity = opt.get();
        if (entity.getStatus() == JobFairPlanStatus.PUBLISH)
            throw new IllegalArgumentException(MessageConstant.JobFair.JOB_FAIR_ALREADY_PUBLISH);
        jobFairRepository.delete(entity);
        return jobFairMapper.toDTO(entity);
    }

    @Override
    public Page<JobFairDTO> findByNameAndCompanyIdAndStatus(String name, String companyId, JobFairPlanStatus status, Pageable pageable) {
        return jobFairRepository.findByNameLikeOrNameIsNullAndCompanyIdAndStatus("%" + name + "%", companyId, status, pageable).map(jobFairMapper::toDTO);
    }


    @Override
    @Transactional
    public void publishJobFair(String companyId, String jobFairId) {
        JobFairEntity jobFairEntity = validateJobFair(companyId, jobFairId);
        jobFairEntity.setStatus(JobFairPlanStatus.PUBLISH);
        jobFairRepository.save(jobFairEntity);
    }

    private JobFairEntity validateJobFair(String companyId, String jobFairId) {
        //check existed job fair
        Optional<JobFairEntity> jobFairEntityOptional = jobFairRepository.findByIdAndCompanyId(jobFairId, companyId);
        if (!jobFairEntityOptional.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.JOB_FAIR_NOT_FOUND));
        }
        JobFairEntity jobFairEntity = jobFairEntityOptional.get();
        Long publicStartTime = jobFairEntity.getPublicStartTime();
        //check job existed job fair in the same time
        List<JobFairEntity> listEntity = jobFairRepository.findByCompanyIdAndStatus(companyId, JobFairPlanStatus.PUBLISH);
        for (JobFairEntity entity : listEntity) {
            if (publicStartTime >= entity.getPublicStartTime()
                    && publicStartTime <= entity.getPublicEndTime())
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.JOB_FAIR_ALREADY_PUBLISH));
        }
        //check job fair basic fields
        JobFairDTO jobFairDTO = jobFairMapper.toDTO(jobFairEntity);
        Set<ConstraintViolation<JobFairDTO>> violations = validator.validate(jobFairDTO);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        validateJobFairTime(jobFairDTO);
        //check job fair is already existed
        if (!jobFairDTO.getStatus().equals(JobFairPlanStatus.DRAFT)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.NOT_EDITABLE));
        }
        return jobFairEntity;
    }

    @Override
    public JobFairDTO validateJobFairForPublish(String companyId, String jobFairId) {
        JobFairEntity jobFairEntity = validateJobFair(companyId, jobFairId);
        return jobFairMapper.toDTO(jobFairEntity);
    }

    @Override
    @Transactional
    public JobFairDTO createOrUpdateJobFairThumbnail(String jobfairThumbnailFolder, String jobFairId, String companyId) {
        String url = awsUtil.generateAwsS3AccessString(jobfairThumbnailFolder, jobFairId);
        Optional<JobFairEntity> jobFairEntityOptional = null;
        if (companyId == null) jobFairEntityOptional = jobFairRepository.findById(jobFairId);
        else jobFairEntityOptional = jobFairRepository.findByIdAndCompanyId(jobFairId, companyId);
        if (!jobFairEntityOptional.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.JOB_FAIR_NOT_FOUND));
        }
        JobFairEntity jobFairEntity = jobFairEntityOptional.get();
        if (!jobFairEntity.getStatus().equals(JobFairPlanStatus.DRAFT)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.NOT_EDITABLE));
        }
        jobFairEntity.setThumbnailUrl(url);
        jobFairRepository.save(jobFairEntity);
        return jobFairMapper.toDTO(jobFairEntity);
    }

    @Override
    public Page<JobFairDTO> findJobFairForAttendantByCriteria(String name, String countryId, String subCategoryId, Pageable pageable) {
        long currentTime = new Date().getTime();
        return jobFairRepository.findJobFairForAttendant
                ("%" + name + "%",
                        JobFairPlanStatus.PUBLISH,
                        currentTime,
                        subCategoryId,
                        pageable).map(jobFairMapper::toDTO);
    }

    @Override
    public JobFairProgressDTO getJobFairProgress(String jobFairId) {
        Optional<JobFairEntity> jobFairOpt = jobFairRepository.findById(jobFairId);
        if (!jobFairOpt.isPresent()){
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.JOB_FAIR_NOT_FOUND));
        }
        JobFairEntity jobFair = jobFairOpt.get();

        JobFairProgressDTO result = new JobFairProgressDTO();
        result.setName(jobFair.getName());

        jobFair.getJobFairBoothList().forEach(jobFairBooth -> {
            List<AssignmentEntity> supervisors = assignmentRepository.findByJobFairBoothIdAndType(jobFairBooth.getId(), AssignmentType.SUPERVISOR);
            if (supervisors.size() != 1){
                return;
            }
            JobFairProgressDTO.Booth booth = new JobFairProgressDTO.Booth();
            booth.setId(jobFairBooth.getId());
            booth.setName(jobFairBooth.getName());
            if (supervisors.size() != 0){
                AssignmentEntity supervisorAssignment = supervisors.get(0);
                JobFairProgressDTO.Supervisor supervisor = new JobFairProgressDTO.Supervisor();
                boolean assignTask = assignmentService.isSupervisorAssignAllTask(jobFairBooth.getId());
                boolean boothProfile = jobFairBooth.getBoothJobPositions().size() > 0 && jobFairBooth.getDescription() != null;
                supervisor.setName(supervisorAssignment.getCompanyEmployee().getAccount().getFullname());
                supervisor.setAssignTask(assignTask);
                supervisor.setBoothProfile(boothProfile);
                booth.setSupervisor(supervisor);
            }
            List<AssignmentEntity> decorators = assignmentRepository.findByJobFairBoothIdAndType(jobFairBooth.getId(), AssignmentType.DECORATOR);
            if (decorators.size() != 0){
                AssignmentEntity decoratorAssignment = decorators.get(0);
                JobFairProgressDTO.Decorator decorator = new JobFairProgressDTO.Decorator();
                Optional<JobFairBoothLayoutDTO> latestLayoutOpt = jobFairBoothLayoutService.getLatestVersionByCompanyBoothId(jobFairBooth.getId());
                boolean decorate = latestLayoutOpt.isPresent();
                decorator.setName(decoratorAssignment.getCompanyEmployee().getAccount().getFullname());
                decorator.setDecorate(decorate);
                booth.setDecorator(decorator);
            }
            double taskDone = 0.0;
            if (booth.getSupervisor().isAssignTask()) taskDone++;
            if (booth.getSupervisor().isBoothProfile()) taskDone++;
            if (booth.getDecorator().isDecorate()) taskDone++;
            booth.setProgress(taskDone / 3);
            result.getBooths().add(booth);
        });

        double overallProgress = result.getBooths().stream().map(booth -> booth.getProgress()).mapToDouble(Double::doubleValue).average().orElse(0);
        result.setOverallProgress(overallProgress);
        return result;
    }
}
