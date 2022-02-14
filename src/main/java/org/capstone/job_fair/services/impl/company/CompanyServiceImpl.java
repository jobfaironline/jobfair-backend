package org.capstone.job_fair.services.impl.company;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.dtos.company.CompanyRegistrationDTO;
import org.capstone.job_fair.models.dtos.company.SubCategoryDTO;
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
import org.capstone.job_fair.services.interfaces.company.CompanyService;
import org.capstone.job_fair.services.mappers.CompanyMapper;
import org.capstone.job_fair.services.mappers.CompanyRegistrationMapper;
import org.capstone.job_fair.services.mappers.JobPositionMapper;
import org.capstone.job_fair.services.mappers.RegistrationJobPositionMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    private SubCategoryRepository subCategoryRepository;


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
    public Optional<CompanyDTO> getCompanyById(String id) {
        return companyRepository.findById(id).map(companyMapper::toDTO);
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
        if (dto.getSizeId() != null && !isSizeIdValid(dto.getSizeId())) {
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
        Optional<CompanyEntity> result = companyRepository.findById(id);
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


}
