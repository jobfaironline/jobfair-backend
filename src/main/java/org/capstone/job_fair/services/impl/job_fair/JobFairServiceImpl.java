package org.capstone.job_fair.services.impl.job_fair;

import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.statuses.JobFairPlanStatus;
import org.capstone.job_fair.repositories.job_fair.JobFairRepository;
import org.capstone.job_fair.services.interfaces.job_fair.JobFairService;
import org.capstone.job_fair.services.mappers.job_fair.JobFairMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class JobFairServiceImpl implements JobFairService {

    @Autowired
    private JobFairRepository jobFairRepository;

    @Autowired
    private JobFairMapper jobFairMapper;

    @Override
    public Optional<JobFairDTO> getById(String id) {
        return jobFairRepository.findById(id).map(jobFairMapper::toDTO);
    }

    @Override
    @Transactional
    public JobFairDTO createNewJobFair(final JobFairDTO dto) {
        long now = new Date().getTime();
        dto.setCreateTime(now);
        dto.setStatus(JobFairPlanStatus.DRAFT);

        if (dto.getDecorateEndTime() < dto.getDecorateStartTime() + DataConstraint.JobFair.MINIMUM_DECORATE_TIME) {
            throw new IllegalArgumentException(MessageConstant.JobFair.INVALID_DECORATE_TIME);
        }
        if (dto.getDecorateEndTime() < dto.getDecorateStartTime() + DataConstraint.JobFair.MINIMUM_PUBLIC_TIME) {
            throw new IllegalArgumentException(MessageConstant.JobFair.INVALID_DECORATE_TIME);
        }
        if (dto.getPublicStartTime() < dto.getDecorateEndTime() + DataConstraint.JobFair.MINIMUM_BUFFER_TIME) {
            throw new IllegalArgumentException(MessageConstant.JobFair.INVALID_BUFFER_TIME);
        }

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


}
