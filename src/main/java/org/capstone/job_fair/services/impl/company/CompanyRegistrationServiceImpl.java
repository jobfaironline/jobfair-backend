package org.capstone.job_fair.services.impl.company;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.company.CompanyRegistrationDTO;
import org.capstone.job_fair.models.dtos.company.job.RegistrationJobPositionDTO;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.capstone.job_fair.models.entities.company.CompanyRegistrationEntity;
import org.capstone.job_fair.models.entities.company.job.JobPositionEntity;
import org.capstone.job_fair.models.entities.company.job.RegistrationJobPositionEntity;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.statuses.CompanyRegistrationStatus;
import org.capstone.job_fair.models.statuses.JobFairPlanStatus;
import org.capstone.job_fair.repositories.company.CompanyEmployeeRepository;
import org.capstone.job_fair.repositories.company.CompanyRegistrationRepository;
import org.capstone.job_fair.repositories.company.job.JobPositionRepository;
import org.capstone.job_fair.repositories.company.job.RegistrationJobPositionRepository;
import org.capstone.job_fair.repositories.job_fair.JobFairRepository;
import org.capstone.job_fair.services.interfaces.company.CompanyRegistrationService;
import org.capstone.job_fair.services.mappers.company.CompanyRegistrationMapper;
import org.capstone.job_fair.services.mappers.company.RegistrationJobPositionMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CompanyRegistrationServiceImpl implements CompanyRegistrationService {

    @Autowired
    private JobFairRepository jobFairRepository;

    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    @Autowired
    private RegistrationJobPositionRepository registrationJobPositionRepository;

    @Autowired
    private CompanyEmployeeRepository companyEmployeeRepository;

    @Autowired
    private JobPositionRepository jobPositionRepository;

    @Autowired
    private RegistrationJobPositionMapper registrationJobPositionMapper;

    @Autowired
    private CompanyRegistrationMapper companyRegistrationMapper;

    private void validateJobFair(JobFairEntity entity, Long currentTime) {
        if (entity.getCompanyRegisterStartTime() > currentTime || entity.getCompanyRegisterEndTime() < currentTime) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyRegistration.JOB_FAIR_REGISTRATION_OUT_OF_REGISTER_TIME));
        }
    }

    private void validateUniqueJobPosition(List<RegistrationJobPositionDTO> jobPositions) {
        jobPositions.sort(Comparator.comparing(RegistrationJobPositionDTO::getId));
        for (int i = 0; i <= jobPositions.size() - 2; i++) {
            RegistrationJobPositionDTO currentDTO = jobPositions.get(i);
            RegistrationJobPositionDTO nextDTO = jobPositions.get(i + 1);
            if (currentDTO.getId().equals(nextDTO.getId())) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyRegistration.UNIQUE_JOB_POSITION_ERROR));
            }
        }
    }

    private void validateRegistrationJobPosition(RegistrationJobPositionDTO dto) {
        if (dto.getMinSalary() != null && dto.getMaxSalary() != null && dto.getMinSalary() > dto.getMaxSalary()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyRegistration.MIN_MAX_SALARY_ERROR));
        }
    }

    @Override
    @Transactional
    public CompanyRegistrationDTO createDraftCompanyRegistration(CompanyRegistrationDTO companyRegistrationDTO, List<RegistrationJobPositionDTO> jobPositions) {
        CompanyRegistrationDTO companyRegistrationDTOReturn = null;

        //Create registration job position entity

        //Check job fair existence
        Optional<JobFairEntity> jobFairOpt = jobFairRepository.findById(companyRegistrationDTO.getJobFairId());
        if (!jobFairOpt.isPresent())
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.JOB_FAIR_NOT_FOUND));
        JobFairEntity jobFairEntity = jobFairOpt.get();
        //Check if job fair has been approved
        if (jobFairEntity.getStatus() != JobFairPlanStatus.APPROVE)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.JOB_FAIR_IS_NOT_APPROVED));
        //Validate job fair registration time of company
        long currentTime = new Date().getTime();
        validateJobFair(jobFairEntity, currentTime);
        //make sure the registration job position is unique
        validateUniqueJobPosition(jobPositions);


        //Get company from user information in security context
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CompanyEmployeeEntity companyEmployee = companyEmployeeRepository.findById(userDetails.getId()).get();

        //Set company id to company registration dto
        companyRegistrationDTO.setCompanyId(companyEmployee.getCompany().getId());
        companyRegistrationDTO.setStatus(CompanyRegistrationStatus.DRAFT);
        //Create company registration entity
        CompanyRegistrationEntity companyRegistrationEntity = companyRegistrationMapper.toEntity(companyRegistrationDTO);
        companyRegistrationEntity.setCreateDate(currentTime);
        companyRegistrationDTOReturn = companyRegistrationMapper.toDTO(companyRegistrationRepository.save(companyRegistrationEntity));

        for (RegistrationJobPositionDTO registrationJobPositionDTO : jobPositions) {
            //Get job position entity by job position id in request
            Optional<JobPositionEntity> jobPositionOpt = jobPositionRepository.findById(registrationJobPositionDTO.getId());
            //Check job position existence
            if (!jobPositionOpt.isPresent())
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.JOB_POSITION_NOT_FOUND));
            JobPositionEntity jobPositionEntity = jobPositionOpt.get();
            //Check if job position entity is belonged to company
            if (!jobPositionEntity.getCompany().getId().equals(companyRegistrationDTO.getCompanyId()))
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.COMPANY_MISMATCH));
            validateRegistrationJobPosition(registrationJobPositionDTO);


            RegistrationJobPositionEntity entity = registrationJobPositionMapper.toEntity(registrationJobPositionDTO);

            //Job position information
            entity.setTitle(jobPositionEntity.getTitle());
            entity.setDescription(jobPositionEntity.getDescription());
            entity.setRequirements(jobPositionEntity.getRequirements());
            entity.setContactPersonName(jobPositionEntity.getContactPersonName());
            entity.setContactEmail(jobPositionEntity.getContactEmail());
            entity.setLanguage(jobPositionEntity.getLanguage());
            entity.setJobLevel(jobPositionEntity.getJobLevel());
            entity.setJobTypeEntity(jobPositionEntity.getJobTypeEntity());
            //Map company registration to registration job position entity
            entity.setCompanyRegistration(companyRegistrationEntity);
            //need new HashSet here to force load lazy-loading entities
            entity.setCategories(new HashSet<>(jobPositionEntity.getCategories()));
            entity.setSkillTagEntities(new HashSet<>(jobPositionEntity.getSkillTagEntities()));


            registrationJobPositionRepository.save(entity);

        }

        return companyRegistrationDTOReturn;
    }

    @Override
    @Transactional
    public void updateDraftCompanyRegistration(CompanyRegistrationDTO companyRegistrationDTO, List<RegistrationJobPositionDTO> jobPositions) {

        //Create registration job position entity

        //Check job fair registration existence
        Optional<CompanyRegistrationEntity> companyRegistrationEntityOpt = companyRegistrationRepository.findById(companyRegistrationDTO.getId());
        if (!companyRegistrationEntityOpt.isPresent())
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyRegistration.NOT_FOUND));
        CompanyRegistrationEntity companyRegistrationEntity = companyRegistrationEntityOpt.get();
        if (!companyRegistrationEntity.getStatus().equals(CompanyRegistrationStatus.DRAFT))
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyRegistration.INVALID_STATUS_WHEN_UPDATE));
        //make sure the registration job position is unique
        validateUniqueJobPosition(jobPositions);
        //Create company registration entity
        companyRegistrationMapper.updateCompanyRegistrationEntityFromDTO(companyRegistrationEntity, companyRegistrationDTO);
        companyRegistrationRepository.save(companyRegistrationEntity);


        for (RegistrationJobPositionDTO registrationJobPositionDTO : jobPositions) {
            //Get job position entity by job position id in request
            Optional<JobPositionEntity> jobPositionOpt = jobPositionRepository.findById(registrationJobPositionDTO.getId());
            //Check job position existence
            if (!jobPositionOpt.isPresent())
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.JOB_POSITION_NOT_FOUND));
            JobPositionEntity jobPositionEntity = jobPositionOpt.get();
            //Check if job position entity is belonged to company
            if (!jobPositionEntity.getCompany().getId().equals(companyRegistrationDTO.getCompanyId()))
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.COMPANY_MISMATCH));
            validateRegistrationJobPosition(registrationJobPositionDTO);

            RegistrationJobPositionEntity entity = registrationJobPositionMapper.toEntity(registrationJobPositionDTO);

            //Job position information
            entity.setTitle(jobPositionEntity.getTitle());
            entity.setDescription(jobPositionEntity.getDescription());
            entity.setRequirements(jobPositionEntity.getRequirements());
            entity.setContactPersonName(jobPositionEntity.getContactPersonName());
            entity.setContactEmail(jobPositionEntity.getContactEmail());
            entity.setLanguage(jobPositionEntity.getLanguage());
            entity.setJobLevel(jobPositionEntity.getJobLevel());
            entity.setJobTypeEntity(jobPositionEntity.getJobTypeEntity());
            //Map company registration to registration job position entity
            entity.setCompanyRegistration(companyRegistrationEntity);
            //need new HashSet here to force load lazy-loading entities
            entity.setCategories(new HashSet<>(jobPositionEntity.getCategories()));
            entity.setSkillTagEntities(new HashSet<>(jobPositionEntity.getSkillTagEntities()));

            registrationJobPositionRepository.save(entity);

        }
    }

    @Override
    @Transactional
    public void submitCompanyRegistration(String registrationId) {
        //check existed registration
        Optional<CompanyRegistrationEntity> registrationEntityOpt = companyRegistrationRepository.findById(registrationId);
        if (!registrationEntityOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyRegistration.NOT_FOUND));
        }
        CompanyRegistrationEntity registrationEntity = registrationEntityOpt.get();
        //Get company from user information in security context
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CompanyEmployeeEntity companyEmployee = companyEmployeeRepository.findById(userDetails.getId()).get();
        String companyId = companyEmployee.getCompany().getId();
        //check if registration belong to company
        if (!registrationEntity.getCompanyId().equals(companyId)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyRegistration.COMPANY_MISMATCH));
        }
        //check for pending registration
        companyRegistrationRepository.findByCompanyIdAndStatus(companyId, CompanyRegistrationStatus.PENDING).ifPresent((entity) -> {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyRegistration.EXISTED_PENDING_REGISTRATION));
        });

        //validate entity
        try {
            Objects.requireNonNull(registrationEntity.getDescription());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.INVALID_JOB_FAIR));
        }
        if (registrationEntity.getRegistrationJobPositions().isEmpty()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.INVALID_JOB_FAIR));
        }

        registrationEntity.setStatus(CompanyRegistrationStatus.PENDING);
        companyRegistrationRepository.save(registrationEntity);

    }

    @Override
    @Transactional
    public void cancelCompanyRegistration(String registrationId, String cancelReason) {
        //check existed registration
        Optional<CompanyRegistrationEntity> registrationEntityOpt = companyRegistrationRepository.findById(registrationId);
        if (!registrationEntityOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyRegistration.NOT_FOUND));
        }
        CompanyRegistrationEntity registrationEntity = registrationEntityOpt.get();
        //Get company from user information in security context
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CompanyEmployeeEntity companyEmployee = companyEmployeeRepository.findById(userDetails.getId()).get();
        String companyId = companyEmployee.getCompany().getId();
        //check if registration belong to company
        if (!registrationEntity.getCompanyId().equals(companyId)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyRegistration.COMPANY_MISMATCH));
        }
        if (registrationEntity.getStatus() != CompanyRegistrationStatus.PENDING) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyRegistration.NOT_ALLOW_CANCEL));
        }
        registrationEntity.setStatus(CompanyRegistrationStatus.CANCEL);
        registrationEntity.setCancelReason(cancelReason);
        companyRegistrationRepository.save(registrationEntity);

    }

    @Override
    @Transactional
    public void staffEvaluateCompanyRegistration(String staffEvaluateCompanyRegistration, CompanyRegistrationStatus status, String message) {
        Optional<CompanyRegistrationEntity> companyRegistrationOpt = companyRegistrationRepository.findById(staffEvaluateCompanyRegistration);
        if (!companyRegistrationOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyRegistration.NOT_FOUND));
        }
        if (status != CompanyRegistrationStatus.REJECT && status != CompanyRegistrationStatus.APPROVE && status != CompanyRegistrationStatus.REQUEST_CHANGE) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyRegistration.INVALID_STATUS_WHEN_EVALUATE));
        }
        if ((status == CompanyRegistrationStatus.REJECT || status == CompanyRegistrationStatus.REQUEST_CHANGE)  && message == null) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyRegistration.REJECT_MISSING_REASON));
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CompanyRegistrationEntity entity = companyRegistrationOpt.get();
        if (entity.getStatus() != CompanyRegistrationStatus.PENDING) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyRegistration.INVALID_COMPANY_REGISTRATION_STATUS_WHEN_EVALUATE));
        }

        entity.setStatus(status);
        if (status == CompanyRegistrationStatus.REJECT || status == CompanyRegistrationStatus.REQUEST_CHANGE) entity.setAdminMessage(message);
        entity.setAuthorizerId(userDetails.getId());
        companyRegistrationRepository.save(entity);
    }

    @Override
    public List<CompanyRegistrationDTO> getCompanyRegistrationByJobFairIDAndCompanyID(String jobFairId, String companyId) {
        List<CompanyRegistrationEntity> companyRegistrationEntities = companyRegistrationRepository.findAllByJobFairIdAndCompanyId(jobFairId, companyId);
        return companyRegistrationEntities.stream().map(companyRegistrationMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Page<CompanyRegistrationDTO> getCompanyRegistrationOfAJobFair(String jobFairId, int offset, int pageSize, String sortBy, Sort.Direction direction) {
        if (offset < DataConstraint.CompanyRegistration.OFFSET_MIN || pageSize < DataConstraint.CompanyRegistration.PAGE_SIZE_MIN)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyRegistration.INVALID_PAGE_NUMBER));
        Page<CompanyRegistrationEntity> companyRegistrationEntityPage = companyRegistrationRepository.findAllByJobFairIdAndStatusIn(jobFairId, Arrays.asList(CompanyRegistrationStatus.APPROVE, CompanyRegistrationStatus.PENDING, CompanyRegistrationStatus.REJECT, CompanyRegistrationStatus.REQUEST_CHANGE), PageRequest.of(offset, pageSize).withSort(Sort.by(direction, sortBy)));
        return companyRegistrationEntityPage.map(entity -> companyRegistrationMapper.toDTO(entity));
    }

    @Override
    public Optional<CompanyRegistrationDTO> getCompanyLatestApproveRegistrationByJobFairIdAndCompanyId(String jobFairId, String companyId) {
        List<CompanyRegistrationEntity> companyRegistrationEntities = companyRegistrationRepository.findAllByJobFairIdAndCompanyIdAndStatus(jobFairId, companyId, CompanyRegistrationStatus.APPROVE);
        companyRegistrationEntities.sort(Comparator.comparing(CompanyRegistrationEntity::getCreateDate));
        if (companyRegistrationEntities.isEmpty()) return Optional.empty();
        return Optional.of(companyRegistrationMapper.toDTO(companyRegistrationEntities.get(0)));
    }

    @Override
    public Optional<CompanyRegistrationDTO> getById(String registrationId) {
        return companyRegistrationRepository.findById(registrationId).map(companyRegistrationMapper::toDTO);
    }
}
