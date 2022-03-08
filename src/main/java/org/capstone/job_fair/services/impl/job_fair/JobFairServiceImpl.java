package org.capstone.job_fair.services.impl.job_fair;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.JobFairConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.job_fair.AdminJobFairStatusDTO;
import org.capstone.job_fair.models.dtos.job_fair.AttendantJobFairStatusDTO;
import org.capstone.job_fair.models.dtos.job_fair.CompanyJobFairStatusDTO;
import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.statuses.JobFairAdminStatus;
import org.capstone.job_fair.models.statuses.JobFairAttendantStatus;
import org.capstone.job_fair.models.statuses.JobFairCompanyStatus;
import org.capstone.job_fair.models.statuses.JobFairPlanStatus;
import org.capstone.job_fair.repositories.account.AccountRepository;
import org.capstone.job_fair.repositories.job_fair.AdminJobFairStatusRepository;
import org.capstone.job_fair.repositories.job_fair.AttendantJobFairStatusRepository;
import org.capstone.job_fair.repositories.job_fair.CompanyJobFairStatusRepository;
import org.capstone.job_fair.repositories.job_fair.JobFairRepository;
import org.capstone.job_fair.services.interfaces.job_fair.JobFairService;
import org.capstone.job_fair.services.mappers.job_fair.AdminJobFairStatusMapper;
import org.capstone.job_fair.services.mappers.job_fair.AttendantJobFairStatusMapper;
import org.capstone.job_fair.services.mappers.job_fair.CompanyJobFairStatusMapper;
import org.capstone.job_fair.services.mappers.job_fair.JobFairMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class JobFairServiceImpl implements JobFairService {
    @Autowired
    JobFairRepository jobFairRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    private JobFairMapper jobFairMapper;
    @Autowired
    private CompanyJobFairStatusRepository companyJobFairStatusRepository;
    @Autowired
    private CompanyJobFairStatusMapper companyJobFairStatusMapper;
    @Autowired
    private AttendantJobFairStatusRepository attendantJobFairStatusRepository;
    @Autowired
    private AttendantJobFairStatusMapper attendantJobFairStatusMapper;
    @Autowired
    private AdminJobFairStatusRepository adminJobFairStatusRepository;
    @Autowired
    private AdminJobFairStatusMapper adminJobFairStatusMapper;


    @Override
    @Transactional
    public void draftJobFair(JobFairDTO dto) {
        dto.setStatus(JobFairPlanStatus.DRAFT);
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        dto.setCreatorId(userDetails.getId());

        long currentTime = new Date().getTime();
        if (Objects.nonNull(dto.getCompanyRegisterStartTime())) {
            if (dto.getCompanyRegisterStartTime() - currentTime > DataConstraint.JobFair.VALID_START_JOB_FAIR_PLAN) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.INVALID_COMPANY_REGISTER_START_TIME));
            }
        }

        if (Objects.nonNull(dto.getCompanyRegisterEndTime()) && Objects.nonNull(dto.getCompanyRegisterStartTime())) {
            if (dto.getCompanyRegisterEndTime() - dto.getCompanyRegisterStartTime() > DataConstraint.JobFair.VALID_REGISTER_TIME)
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.INVALID_END_TIME));
        }

        if (Objects.nonNull(dto.getCompanyRegisterEndTime()) && Objects.nonNull(dto.getCompanyBuyBoothStartTime())) {
            if (dto.getCompanyRegisterEndTime() - dto.getCompanyBuyBoothStartTime() > DataConstraint.JobFair.VALID_REGISTER_TO_BUY_BOOTH_TIME)
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.END_TIME_LESS_THAN_START_TIME_ERROR));
        }

        if (Objects.nonNull(dto.getCompanyBuyBoothEndTime()) && Objects.nonNull(dto.getCompanyBuyBoothStartTime())) {
            if (dto.getCompanyBuyBoothEndTime() - dto.getCompanyBuyBoothStartTime() > DataConstraint.JobFair.VALID_BUY_BOOTH_TIME)
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.INVALID_END_TIME));
        }

        if (Objects.nonNull(dto.getCompanyBuyBoothEndTime()) && Objects.nonNull(dto.getAttendantRegisterStartTime())) {
            if (dto.getCompanyBuyBoothEndTime() - dto.getAttendantRegisterStartTime() > DataConstraint.JobFair.VALID_BUY_BOOTH_TO_PUBLIC_TIME)
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.END_TIME_LESS_THAN_START_TIME_ERROR));
        }

        if (Objects.nonNull(dto.getAttendantRegisterStartTime()) && Objects.nonNull(dto.getStartTime())) {
            if (dto.getAttendantRegisterStartTime() - dto.getStartTime() > DataConstraint.JobFair.VALID_PUBLIC_TO_EVENT_TIME)
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.END_TIME_LESS_THAN_START_TIME_ERROR));
        }

        if (Objects.nonNull(dto.getEndTime()) && Objects.nonNull(dto.getStartTime())) {
            if (dto.getEndTime() - dto.getStartTime() > DataConstraint.JobFair.VALID_EVENT_TIME)
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.INVALID_END_TIME));
        }

        JobFairEntity entity = jobFairMapper.toJobFairEntity(dto);
        jobFairRepository.save(entity);
    }

    @Override
    public List<JobFairDTO> getAllJobFairPlanOfCurrentAccount() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String id = userDetails.getId();
        List<JobFairEntity> entities = jobFairRepository.findAllByCreatorId(id);
        if (entities.isEmpty()) return null;
        return entities.stream()
                .map(jobFairEntity -> jobFairMapper.toJobFairDTO(jobFairEntity))
                .collect(Collectors.toList());
    }

    private JobFairEntity getValidatedJobFair(String jobFairId, JobFairPlanStatus status) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String creatorId = userDetails.getId();
        Optional<JobFairEntity> entity = jobFairRepository.findById(jobFairId);
        if (!entity.isPresent()) throw new IllegalArgumentException(
                MessageUtil.getMessage(MessageConstant.JobFair.JOB_FAIR_NOT_FOUND)
        );
        if (!entity.get().getCreatorId().equals(creatorId)) throw new IllegalArgumentException(
                MessageUtil.getMessage(MessageConstant.JobFair.INVALID_CREATOR_ID)
        );
        if (!entity.get().getStatus().toString().equals(status.toString())) throw new IllegalArgumentException(
                MessageUtil.getMessage(MessageConstant.JobFair.INVALID_JOB_FAIR_STATUS)
        );
        return entity.get();
    }

    @Override
    @Transactional
    public void deleteJobFairDraft(String jobFairId) {
        JobFairEntity entity = getValidatedJobFair(jobFairId, JobFairPlanStatus.DRAFT);
        entity.setStatus(JobFairPlanStatus.DELETED);
        jobFairRepository.save(entity);
    }

    @Override
    @Transactional
    public void submitJobFairDraft(String jobFairId) {
        JobFairEntity entity = getValidatedJobFair(jobFairId, JobFairPlanStatus.DRAFT);

        long currentTime = new Date().getTime();

        try {
            Objects.requireNonNull(entity.getCompanyRegisterStartTime());
            Objects.requireNonNull(entity.getCompanyRegisterEndTime());
            Objects.requireNonNull(entity.getCompanyBuyBoothStartTime());
            Objects.requireNonNull(entity.getCompanyBuyBoothEndTime());
            Objects.requireNonNull(entity.getAttendantRegisterStartTime());
            Objects.requireNonNull(entity.getCompanyRegisterStartTime());
            Objects.requireNonNull(entity.getStartTime());
            Objects.requireNonNull(entity.getEndTime());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.INVALID_JOB_FAIR));
        }

        if (entity.getCompanyRegisterStartTime() - currentTime > DataConstraint.JobFair.VALID_START_JOB_FAIR_PLAN) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.INVALID_COMPANY_REGISTER_START_TIME));
        }

        if (entity.getCompanyRegisterEndTime() - entity.getCompanyRegisterStartTime() > DataConstraint.JobFair.VALID_REGISTER_TIME)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.INVALID_END_TIME));

        if (entity.getCompanyRegisterEndTime() - entity.getCompanyBuyBoothStartTime() > DataConstraint.JobFair.VALID_REGISTER_TO_BUY_BOOTH_TIME)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.END_TIME_LESS_THAN_START_TIME_ERROR));

        if (entity.getCompanyBuyBoothEndTime() - entity.getCompanyBuyBoothStartTime() > DataConstraint.JobFair.VALID_BUY_BOOTH_TIME)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.INVALID_END_TIME));

        if (entity.getCompanyBuyBoothEndTime() - entity.getAttendantRegisterStartTime() > DataConstraint.JobFair.VALID_BUY_BOOTH_TO_PUBLIC_TIME)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.END_TIME_LESS_THAN_START_TIME_ERROR));

        if (entity.getAttendantRegisterStartTime() - entity.getStartTime() > DataConstraint.JobFair.VALID_PUBLIC_TO_EVENT_TIME)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.END_TIME_LESS_THAN_START_TIME_ERROR));

        if (entity.getEndTime() - entity.getStartTime() > DataConstraint.JobFair.VALID_EVENT_TIME)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.INVALID_END_TIME));

        entity.setStatus(JobFairPlanStatus.PENDING);
        jobFairRepository.save(entity);
    }

    @Override
    @Transactional
    public void cancelPendingJobFair(String jobFairId, String reason) {
        JobFairEntity entity = getValidatedJobFair(jobFairId, JobFairPlanStatus.PENDING);
        entity.setCancelReason(reason);
        entity.setStatus(JobFairPlanStatus.CANCEL);
        jobFairRepository.save(entity);
    }

    @Override
    @Transactional
    public void restoreDeletedJobFair(String jobFairId) {
        JobFairEntity entity = getValidatedJobFair(jobFairId, JobFairPlanStatus.DELETED);
        entity.setStatus(JobFairPlanStatus.DRAFT);
        jobFairRepository.save(entity);
    }

    @Override
    @Transactional
    public void adminEvaluateJobFair(String jobFairId, JobFairPlanStatus status, String message) {
        Optional<JobFairEntity> jobFairEntityOpt = jobFairRepository.findById(jobFairId);
        if (!jobFairEntityOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.JOB_FAIR_NOT_FOUND));
        }
        if (status != JobFairPlanStatus.REJECT && status != JobFairPlanStatus.APPROVE) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.INVALID_STATUS_WHEN_EVALUATE));
        }
        if (status == JobFairPlanStatus.REJECT && message == null) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.REJECT_MISSING_REASON));
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();


        JobFairEntity entity = jobFairEntityOpt.get();
        entity.setStatus(status);
        entity.setRejectReason(message);
        entity.setAuthorizerId(userDetails.getId());
        jobFairRepository.save(entity);
    }

    @Override
    public Page<JobFairDTO> getAllForAdmin(JobFairPlanStatus status, int offset, int pageSize, String sortBy, Sort.Direction direction) {
        if (offset < DataConstraint.CompanyRegistration.OFFSET_MIN || pageSize < DataConstraint.CompanyRegistration.PAGE_SIZE_MIN)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.INVALID_PAGE_NUMBER));
        if (status != null && status.equals(JobFairPlanStatus.DRAFT))
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.INVALID_JOB_FAIR_SEARCH_STATUS_OF_ADMIN));
        if (status == null) {
            Page<JobFairEntity> page = jobFairRepository.findAllByStatusNot(JobFairPlanStatus.DRAFT, PageRequest.of(offset, pageSize).withSort(Sort.by(direction, sortBy)));
            return page.map(entity -> jobFairMapper.toJobFairDTO(entity));
        }
        return jobFairRepository.findByStatus(status, PageRequest.of(offset, pageSize).withSort(Sort.by(direction, sortBy))).map(entity -> jobFairMapper.toJobFairDTO(entity));
    }

    @Override
    public List<JobFairDTO> getAllJobFairByStatus(JobFairPlanStatus jobFairPlanStatus) {
        List<JobFairEntity> jobFairEntities = jobFairRepository.findAllByStatus(jobFairPlanStatus);
        return jobFairEntities.stream().map(jobFairMapper::toJobFairDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<JobFairDTO> getJobFairByID(String id) {
        return jobFairRepository.findById(id).map(jobFairMapper::toJobFairDTO);
    }

    @Override
    public List<JobFairDTO> getAllAvailableForRegistration(String fromTime, String toTime) {
        long searchFromTime;
        long searchToTime;
        //if fromTime and toTime are null then set time range to be +-1 year from now
        if (fromTime == null) searchFromTime = new Date().getTime() - JobFairConstant.ONE_YEAR_IN_MILLISECOND;
        else searchFromTime = Long.parseLong(fromTime);
        if (toTime == null) searchToTime = new Date().getTime() + JobFairConstant.ONE_YEAR_IN_MILLISECOND;
        else searchToTime = Long.parseLong(toTime);
        if (searchFromTime > searchToTime)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.INVALID_SEARCH_TIME_RANGE));
        List<JobFairEntity> jobFairEntities = jobFairRepository.findAllByStatusAndCompanyRegisterStartTimeGreaterThanAndCompanyRegisterEndTimeLessThan(JobFairPlanStatus.APPROVE, searchFromTime, searchToTime);
        return jobFairEntities.stream().map(jobFairMapper::toJobFairDTO).collect(Collectors.toList());

    }

    @Transactional
    public void updateJobFairDraft(JobFairDTO dto) {
        JobFairEntity jobFairEntity = getValidatedJobFair(dto.getId(), JobFairPlanStatus.DRAFT);

        long currentTime = new Date().getTime();
        if (Objects.nonNull(dto.getCompanyRegisterStartTime())) {
            if (dto.getCompanyRegisterStartTime() - currentTime > DataConstraint.JobFair.VALID_START_JOB_FAIR_PLAN) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.INVALID_COMPANY_REGISTER_START_TIME));
            }
        }

        if (Objects.nonNull(dto.getCompanyRegisterEndTime()) && Objects.nonNull(dto.getCompanyRegisterStartTime())) {
            if (dto.getCompanyRegisterEndTime() - dto.getCompanyRegisterStartTime() > DataConstraint.JobFair.VALID_REGISTER_TIME)
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.INVALID_END_TIME));
        }

        if (Objects.nonNull(dto.getCompanyRegisterEndTime()) && Objects.nonNull(dto.getCompanyBuyBoothStartTime())) {
            if (dto.getCompanyRegisterEndTime() - dto.getCompanyBuyBoothStartTime() > DataConstraint.JobFair.VALID_REGISTER_TO_BUY_BOOTH_TIME)
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.END_TIME_LESS_THAN_START_TIME_ERROR));
        }

        if (Objects.nonNull(dto.getCompanyBuyBoothEndTime()) && Objects.nonNull(dto.getCompanyBuyBoothStartTime())) {
            if (dto.getCompanyBuyBoothEndTime() - dto.getCompanyBuyBoothStartTime() > DataConstraint.JobFair.VALID_BUY_BOOTH_TIME)
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.INVALID_END_TIME));
        }

        if (Objects.nonNull(dto.getCompanyBuyBoothEndTime()) && Objects.nonNull(dto.getAttendantRegisterStartTime())) {
            if (dto.getCompanyBuyBoothEndTime() - dto.getAttendantRegisterStartTime() > DataConstraint.JobFair.VALID_BUY_BOOTH_TO_PUBLIC_TIME)
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.END_TIME_LESS_THAN_START_TIME_ERROR));
        }

        if (Objects.nonNull(dto.getAttendantRegisterStartTime()) && Objects.nonNull(dto.getStartTime())) {
            if (dto.getAttendantRegisterStartTime() - dto.getStartTime() > DataConstraint.JobFair.VALID_PUBLIC_TO_EVENT_TIME)
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.END_TIME_LESS_THAN_START_TIME_ERROR));
        }

        if (Objects.nonNull(dto.getEndTime()) && Objects.nonNull(dto.getStartTime())) {
            if (dto.getEndTime() - dto.getStartTime() > DataConstraint.JobFair.VALID_EVENT_TIME)
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.INVALID_END_TIME));
        }

        jobFairMapper.updateFromDTO(jobFairEntity, dto);
        jobFairRepository.save(jobFairEntity);
    }

    @Override
    public Page<CompanyJobFairStatusDTO> getJobFairForCompany(String companyId, JobFairCompanyStatus status, int offset, int pageSize) {
        if (offset < DataConstraint.Application.OFFSET_MIN || pageSize < DataConstraint.Application.PAGE_SIZE_MIN)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.INVALID_PAGE_NUMBER));
        if (status == null) {
            return companyJobFairStatusRepository
                    .getAllByCompanyId(companyId, PageRequest.of(offset, pageSize))
                    .map(companyJobFairStatusMapper::toDTO);
        }
        return companyJobFairStatusRepository
                .getByStatusAndCompanyId(status, companyId, PageRequest.of(offset, pageSize))
                .map(companyJobFairStatusMapper::toDTO);

    }

    @Override
    public Page<AttendantJobFairStatusDTO> getJobFairForAttendant(String attendantId, JobFairAttendantStatus status, int offset, int pageSize) {
        if (offset < DataConstraint.Application.OFFSET_MIN || pageSize < DataConstraint.Application.PAGE_SIZE_MIN)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.INVALID_PAGE_NUMBER));
        if (status == null) {
            return attendantJobFairStatusRepository
                    .getAllByAttendantId(attendantId, PageRequest.of(offset, pageSize))
                    .map(attendantJobFairStatusMapper::toDTO);
        }
        return attendantJobFairStatusRepository
                .getByStatusAndAttendantId(status, attendantId, PageRequest.of(offset, pageSize))
                .map(attendantJobFairStatusMapper::toDTO);
    }

    @Override
    public Page<AdminJobFairStatusDTO> getJobFairForAdmin(JobFairAdminStatus status, int offset, int pageSize) {
        if (offset < DataConstraint.Paging.OFFSET_MIN || pageSize < DataConstraint.Paging.PAGE_SIZE_MIN)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.INVALID_PAGE_NUMBER));
        if (status == null) {
            return adminJobFairStatusRepository
                    .findAll(PageRequest.of(offset, pageSize))
                    .map(adminJobFairStatusMapper::toDTO);
        }
        return adminJobFairStatusRepository
                .getByStatus(status, PageRequest.of(offset, pageSize))
                .map(adminJobFairStatusMapper::toDTO);
    }

}
