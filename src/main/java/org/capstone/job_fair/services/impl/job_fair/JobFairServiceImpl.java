package org.capstone.job_fair.services.impl.job_fair;

import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.statuses.JobFairPlanStatus;
import org.capstone.job_fair.repositories.job_fair.JobFairRepository;
import org.capstone.job_fair.services.interfaces.job_fair.JobFairService;
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
import java.util.Date;
import java.util.Optional;
import java.util.Set;

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
        jobFairMapper.updateFromDTO(entity, dto);
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
        jobFairRepository.delete(entity);
        return jobFairMapper.toDTO(entity);
    }

    @Override
    public Page<JobFairDTO> findByNameAndCompanyId(String name, String companyId, Pageable pageable) {
        return jobFairRepository.findByNameLikeOrNameIsNullAndCompanyId("%" + name + "%", companyId, pageable).map(jobFairMapper::toDTO);
    }


    @Override
    @Transactional
    public void publishJobFair(String companyId, String jobFairId) {

        Optional<JobFairEntity> jobFairEntityOptional = jobFairRepository.findByIdAndCompanyId(jobFairId, companyId);
        if (!jobFairEntityOptional.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.JOB_FAIR_NOT_FOUND));
        }

        JobFairEntity jobFairEntity = jobFairEntityOptional.get();
        JobFairDTO jobFairDTO = jobFairMapper.toDTO(jobFairEntity);

        Set<ConstraintViolation<JobFairDTO>> violations = validator.validate(jobFairDTO);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        validateJobFairTime(jobFairDTO);

        if (!jobFairDTO.getStatus().equals(JobFairPlanStatus.DRAFT)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.NOT_EDITABLE));
        }

        jobFairEntity.setStatus(JobFairPlanStatus.PUBLISH);

        jobFairRepository.save(jobFairEntity);
    }

    @Override
    public JobFairDTO createOrUpdateJobFairThumbnail(String jobfairThumbnailFolder, String layoutId, String companyId) {
        String url = awsUtil.generateAwsS3AccessString(jobfairThumbnailFolder, layoutId);
        Optional<JobFairEntity> jobFairEntityOptional = null;
        if (companyId == null) jobFairEntityOptional = jobFairRepository.findById(layoutId);
        else jobFairEntityOptional = jobFairRepository.findByIdAndCompanyId(layoutId, companyId);
        if (!jobFairEntityOptional.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.JOB_FAIR_NOT_FOUND));
        }
        JobFairEntity jobFairEntity = jobFairEntityOptional.get();
        if (!jobFairEntity.getStatus().equals(JobFairPlanStatus.DRAFT)){
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.NOT_EDITABLE));
        }
        jobFairEntity.setThumbnailUrl(url);
        jobFairRepository.save(jobFairEntity);
        return jobFairMapper.toDTO(jobFairEntity);
    }
}
