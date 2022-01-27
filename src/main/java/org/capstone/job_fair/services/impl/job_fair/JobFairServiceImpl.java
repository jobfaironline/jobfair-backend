package org.capstone.job_fair.services.impl.job_fair;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.models.statuses.JobFairStatus;
import org.capstone.job_fair.repositories.account.AccountRepository;
import org.capstone.job_fair.repositories.job_fair.JobFairRepository;
import org.capstone.job_fair.services.interfaces.job_fair.JobFairService;
import org.capstone.job_fair.services.mappers.JobFairMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobFairServiceImpl implements JobFairService {
    @Autowired
    JobFairRepository jobFairRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    private JobFairMapper jobFairMapper;

    @Override
    public void createJobFair(JobFairDTO dto) {
        dto.setStatus(JobFairStatus.DRAFT);
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        dto.setCreatorId(userDetails.getId());
        if(dto.getCompanyBuyBoothEndTime() < dto.getCompanyBuyBoothStartTime())
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.END_TIME_LESS_THAN_START_TIME_ERROR));
        if(dto.getCompanyRegisterEndTime() < dto.getCompanyRegisterStartTime())
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.END_TIME_LESS_THAN_START_TIME_ERROR));
        if(dto.getEndTime() < dto.getStartTime())
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.END_TIME_LESS_THAN_START_TIME_ERROR));
        JobFairEntity entity = jobFairMapper.toJobFairEntity(dto);
        jobFairRepository.save(entity);
    }

    @Override
    public List<JobFairDTO> getAllJobFairPlanOfEmployee() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String id = userDetails.getId();
       Optional<AccountEntity> account = accountRepository.findById(id);
        if(!account.isPresent()) throw new IllegalArgumentException(
                MessageUtil.getMessage(MessageConstant.Account.NOT_FOUND));
        System.out.println(account.get().getRole().toString());
        if(!account.get().getRole().getName().equals(Role.STAFF.toString())) throw new
                IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyEmployee.INVALID_ROLE));
        List<JobFairEntity> entities = jobFairRepository.findAllByCreatorId(id);
        if (entities.isEmpty()) return null;
        return  entities.stream().map(jobFairEntity -> {
            return  jobFairMapper.toJobFairDTO(jobFairEntity);
        }).collect(Collectors.toList());
    }
    private JobFairEntity getValidatedJobFair(String jobFairId, JobFairStatus status){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String creatorId = userDetails.getId();
        Optional<JobFairEntity> entity = jobFairRepository.findById(jobFairId);
        if(!entity.isPresent()) throw new IllegalArgumentException(
                MessageUtil.getMessage(MessageConstant.JobFair.JOB_FAIR_NOT_FOUND)
        );
        if(!entity.get().getCreatorId().equals(creatorId)) throw new IllegalArgumentException(
                MessageUtil.getMessage(MessageConstant.JobFair.INVALID_CREATOR_ID)
        );
        if(!entity.get().getStatus().toString().equals(status.toString())) throw new IllegalArgumentException(
                MessageUtil.getMessage(MessageConstant.JobFair.INVALID_JOB_FAIR_STATUS)
        );
        return entity.get();
    }

    @Override
    public void deleteJobFairDraft(String jobFairId) {
        JobFairEntity entity = getValidatedJobFair(jobFairId, JobFairStatus.DRAFT);
        entity.setStatus(JobFairStatus.DELETED);
        jobFairRepository.save(entity);
    }

    @Override
    public void submitJobFairDraft(String jobFairId) {
        JobFairEntity entity = getValidatedJobFair(jobFairId, JobFairStatus.DRAFT);
        entity.setStatus(JobFairStatus.PENDING);
        jobFairRepository.save(entity);
    }

    @Override
    public void cancelPendingJobFair(String jobFairId) {
        JobFairEntity entity = getValidatedJobFair(jobFairId, JobFairStatus.PENDING);
        entity.setStatus(JobFairStatus.CANCEL);
        jobFairRepository.save(entity);
    }

    @Override
    public void restoreDeletedJobFair(String jobFairId) {
        JobFairEntity entity = getValidatedJobFair(jobFairId, JobFairStatus.DELETED);
        entity.setStatus(JobFairStatus.CANCEL);
        jobFairRepository.save(entity);
    }

}
