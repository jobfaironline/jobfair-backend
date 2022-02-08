package org.capstone.job_fair.services.impl.company;

import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.dtos.company.CompanyRegistrationDTO;
import org.capstone.job_fair.models.dtos.company.SubCategoryDTO;
import org.capstone.job_fair.models.dtos.company.job.RegistrationJobPositionDTO;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.models.entities.company.CompanyRegistrationEntity;
import org.capstone.job_fair.models.entities.company.job.RegistrationJobPositionEntity;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.statuses.CompanyRegistrationStatus;
import org.capstone.job_fair.models.statuses.CompanyStatus;
import org.capstone.job_fair.repositories.company.*;
import org.capstone.job_fair.repositories.company.job.RegistrationJobPositionRepository;
import org.capstone.job_fair.repositories.job_fair.JobFairRepository;
import org.capstone.job_fair.services.interfaces.company.CompanyService;
import org.capstone.job_fair.services.mappers.CompanyMapper;
import org.capstone.job_fair.services.mappers.JobPositionMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void registerAJobFair(CompanyRegistrationDTO company, List<RegistrationJobPositionDTO> jobPositions) {
        //Check job fair existence
        JobFairEntity jobFairEntity = jobFairRepository.getById(company.getJobFairId());
        if (jobFairEntity == null) throw new IllegalArgumentException(
                MessageUtil.getMessage(MessageConstant.JobFair.JOB_FAIR_NOT_FOUND));
        //Validate job fair registration time of company
        long time = new Date().getTime();
        if(jobFairEntity.getCompanyRegisterStartTime() > time || jobFairEntity.getCompanyRegisterEndTime() < time)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Company.JOB_FAIR_REGISTRATION_OUT_OF_REGISTER_TIME));
        //Check company existence
        Optional<CompanyEntity> companyEntity = companyRepository.findById(company.getCompanyId());
        if (!companyEntity.isPresent()) throw new IllegalArgumentException(
                MessageUtil.getMessage(MessageConstant.Company.NOT_FOUND));
        company.setStatus(CompanyRegistrationStatus.DRAFT);
        CompanyRegistrationEntity companyRegistrationEntity = companyMapper.toEntity(company);
        companyRegistrationEntity.setCreateDate(time);
        companyRegistrationRepository.save(companyRegistrationEntity);
        for (RegistrationJobPositionDTO job : jobPositions) {
            if(job.getMaxSalary() < job.getMinSalary())
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.SALARY_ERROR));
            RegistrationJobPositionEntity entity = jobPositionMapper.toEntity(job);
            entity.setCompanyRegistration(companyRegistrationEntity);
            registrationJobPositionRepository.save(entity);
        }
    }
}
