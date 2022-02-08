package org.capstone.job_fair.services.impl.company;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.CompanyJobFairRegistrationRequest;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.dtos.company.CompanyRegistrationDTO;
import org.capstone.job_fair.models.dtos.company.SubCategoryDTO;
import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.models.dtos.company.job.RegistrationJobPositionDTO;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.models.entities.company.CompanyRegistrationEntity;
import org.capstone.job_fair.models.entities.company.job.JobPositionEntity;
import org.capstone.job_fair.models.entities.company.job.RegistrationJobPositionEntity;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.statuses.CompanyRegistrationStatus;
import org.capstone.job_fair.models.statuses.CompanyStatus;
import org.capstone.job_fair.repositories.company.*;
import org.capstone.job_fair.repositories.company.job.JobPositionRepository;
import org.capstone.job_fair.repositories.company.job.RegistrationJobPositionRepository;
import org.capstone.job_fair.repositories.job_fair.JobFairRepository;
import org.capstone.job_fair.services.interfaces.company.CompanyEmployeeService;
import org.capstone.job_fair.services.interfaces.company.CompanyService;
import org.capstone.job_fair.services.mappers.CompanyMapper;
import org.capstone.job_fair.services.mappers.JobPositionMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMapper mapper;

    @Autowired
    private CompanySizeRepository companySizeRepository;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private BenefitRepository benefitRepository;

    @Autowired
    private JobFairRepository jobFairRepository;

    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private RegistrationJobPositionRepository registrationJobPositionRepository;

    @Autowired
    private JobPositionMapper jobPositionMapper;

    @Autowired
    private CompanyEmployeeRepository companyEmployeeRepository;

    @Autowired
    private JobPositionRepository jobPositionRepository;



    private boolean isEmailExisted(String email) {
        return companyRepository.countByEmail(email) != 0;
    }

    private boolean isTaxIDExisted(String taxID) {
        return companyRepository.countByTaxId(taxID) != 0;
    }

    private boolean isSizeIdValid(int id) {
        return companySizeRepository.existsById(id);
    }

    private boolean isSubCategoryIdValid(int id) {
        return subCategoryRepository.existsById(id);
    }

    private boolean isBenefitIdValid(int id) {
        return benefitRepository.existsById(id);
    }

    private void checkCompanySubCategory(CompanyDTO dto) {
        if (dto.getSubCategoryDTOs() != null) {
            dto.getSubCategoryDTOs().stream().map(SubCategoryDTO::getId).forEach(id -> {
                if (!isSubCategoryIdValid(id))
                    throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.SubCategory.NOT_FOUND));
            });
        }
    }

    private void checkCompanyBenefit(CompanyDTO dto) {
        if (dto.getCompanyBenefitDTOS() != null) {
            dto.getCompanyBenefitDTOS().stream()
                    .map(companyBenefitDTO -> companyBenefitDTO.getBenefitDTO().getId())
                    .forEach(id -> {
                        if (!isBenefitIdValid(id))
                            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Benefit.NOT_FOUND));
                    });
        }
    }


    private void checkCompanyDTOValid(CompanyDTO companyDTO) {
        checkCompanySubCategory(companyDTO);
        checkCompanyBenefit(companyDTO);
    }


    @Override
    public List<CompanyDTO> getAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(CompanyEntity -> mapper.toDTO(CompanyEntity))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CompanyEntity> getCompanyById(String id) {
        return companyRepository.findById(id);
    }


    @Override
    public void createCompany(CompanyDTO dto) {
        if (isEmailExisted(dto.getEmail())) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Company.EMAIL_EXISTED));
        }
        if (isTaxIDExisted(dto.getTaxId())) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Company.TAX_ID_EXISTED));
        }

        if (!isSizeIdValid(dto.getSizeId())) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Company.SIZE_INVALID));
        }
        checkCompanyDTOValid(dto);

        dto.setStatus(CompanyStatus.REGISTERED);
        dto.setEmployeeMaxNum(DataConstraint.Company.DEFAULT_EMPLOYEE_MAX_NUM);

        CompanyEntity entity = mapper.toEntity(dto);
        if (entity.getCompanyBenefits() != null) {
            entity.getCompanyBenefits().forEach(companyBenefitEntity -> companyBenefitEntity.setCompany(entity));
        }

        companyRepository.save(entity);
    }

    @Transactional
    @Override
    public void updateCompany(CompanyDTO dto) {
        Optional<CompanyEntity> opt = companyRepository.findById(dto.getId());

        if (!opt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Company.NOT_FOUND));
        }
        if (dto.getEmail() != null
                && !opt.get().getEmail().equals(dto.getEmail())
                && isEmailExisted(dto.getEmail())) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Company.EMAIL_EXISTED));
        }
        if (dto.getTaxId() != null
                && !opt.get().getTaxId().equals(dto.getTaxId())
                && isTaxIDExisted(dto.getTaxId())) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Company.TAX_ID_EXISTED));
        }
        if (dto.getTaxId() != null && isSizeIdValid(dto.getSizeId())) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Company.SIZE_INVALID));
        }
        checkCompanyDTOValid(dto);

        CompanyEntity entity = companyRepository.getById(dto.getId());
        companyMapper.updateCompanyEntity(dto, entity);
        entity.getCompanyBenefits().forEach(companyBenefitEntity -> companyBenefitEntity.setCompany(entity));

        companyRepository.save(entity);
    }

    @Transactional
    @Override
    public Boolean deleteCompany(String id) {
        Optional<CompanyEntity> result = this.getCompanyById(id);
        if (result.isPresent()) {
            companyRepository.delete(result.get());
            return true;
        }
        return false;
    }

    @Override
    public Integer getCountById(String id) {
        return companyRepository.countById(id);
    }

    @Override
    public Optional<CompanyEntity> findCompanyById(String id) {
        return companyRepository.findById(id);
    }

    @Override
    @Transactional
    public void registerAJobFair(CompanyRegistrationDTO companyRegistrationDTO, List<CompanyJobFairRegistrationRequest.JobPosition> jobPositions) {

        //Create registration job position entity

        //Check job fair existence
        Optional<JobFairEntity> jobFairEntity = jobFairRepository.findById(companyRegistrationDTO.getJobFairId());
        if (!jobFairEntity.isPresent()) throw new IllegalArgumentException(
                MessageUtil.getMessage(MessageConstant.JobFair.JOB_FAIR_NOT_FOUND));

        //Validate job fair registration time of company
        long time = new Date().getTime();
//        if(jobFairEntity.get().getCompanyRegisterStartTime() > time || jobFairEntity.get().getCompanyRegisterEndTime() < time)
//            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Company.JOB_FAIR_REGISTRATION_OUT_OF_REGISTER_TIME));
        //Get company from user information in security context
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        CompanyEmployeeEntity companyEmployee = companyEmployeeRepository.findById(userDetails.getId()).get();

        //Set company id to company registration dto
        companyRegistrationDTO.setCompanyId(companyEmployee.getCompany().getId());
        companyRegistrationDTO.setStatus(CompanyRegistrationStatus.DRAFT);
        //Create company registration entity
        CompanyRegistrationEntity companyRegistrationEntity = companyMapper.toEntity(companyRegistrationDTO);
        companyRegistrationEntity.setCreateDate(time);
        companyRegistrationEntity.setCompanyId(companyEmployee.getCompany().getId());
        companyRegistrationRepository.save(companyRegistrationEntity);

        for (CompanyJobFairRegistrationRequest.JobPosition jobPositionRequest:jobPositions) {
            //Get job position entity by job position id in request
            Optional<JobPositionEntity> jobPosition =
                    jobPositionRepository.findById(jobPositionRequest.getJobPositionId());
            //Check job position existence
            if(!jobPosition.isPresent()) throw new IllegalArgumentException(
                    MessageUtil.getMessage(MessageConstant.Job.JOB_POSITION_NOT_FOUND));
            //Check if job position entity is belonged to company
            if(!jobPosition.get().getCompany().getId().equals(companyRegistrationDTO.getCompanyId()))
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.COMPANY_MISMATCH));


            RegistrationJobPositionDTO registrationJobPositionDTO = new RegistrationJobPositionDTO();
            JobPositionDTO jobPositionDTO = jobPositionMapper.toDTO(jobPosition.get());
            //Job position information
            registrationJobPositionDTO.setTitle(jobPositionDTO.getTitle());
            registrationJobPositionDTO.setContactPersonName(jobPositionDTO.getContactPersonName());
            registrationJobPositionDTO.setContactEmail(jobPositionDTO.getContactEmail());
            registrationJobPositionDTO.setLanguage(jobPositionDTO.getLanguage());
            registrationJobPositionDTO.setJobLevel(jobPositionDTO.getLevel());
            registrationJobPositionDTO.setJobType(jobPositionDTO.getJobType());
            //Map company registration to registration job position entity
            registrationJobPositionDTO.setCompanyRegistration(companyRegistrationDTO);
            registrationJobPositionDTO.setSubCategoryDTOs(jobPositionDTO.getSubCategoryDTOs());
            registrationJobPositionDTO.setSkillTagDTOS(jobPositionDTO.getSkillTagDTOS());

            //Additional information of job position
            //Map additional information from job position entity to registration job postiion entity
            registrationJobPositionDTO.setDescription(jobPositionRequest.getDescription());
            registrationJobPositionDTO.setRequirements(jobPositionRequest.getRequirements());
            registrationJobPositionDTO.setMinSalary(jobPositionRequest.getMinSalary());
            registrationJobPositionDTO.setMaxSalary(jobPositionRequest.getMaxSalary());
            registrationJobPositionDTO.setNumOfPosition(jobPositionRequest.getNumOfPosition());

            RegistrationJobPositionEntity registrationJobPositionEntity = jobPositionMapper.toEntity(registrationJobPositionDTO);
            registrationJobPositionEntity.setCompanyRegistration(companyRegistrationEntity);

            registrationJobPositionRepository.save(registrationJobPositionEntity);

        }
    }
}
