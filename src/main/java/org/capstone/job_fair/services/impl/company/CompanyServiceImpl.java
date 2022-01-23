package org.capstone.job_fair.services.impl.company;

import org.capstone.job_fair.models.dtos.company.BenefitDTO;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.dtos.company.MediaDTO;
import org.capstone.job_fair.models.dtos.company.SubCategoryDTO;
import org.capstone.job_fair.models.entities.company.*;
import org.capstone.job_fair.repositories.company.CompanyRepository;
import org.capstone.job_fair.repositories.company.MediaRepository;
import org.capstone.job_fair.services.interfaces.company.CompanyService;
import org.capstone.job_fair.services.interfaces.company.CompanySizeService;
import org.capstone.job_fair.services.mappers.CompanyEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private static final String NOT_FOUND_SIZE = "Size not found with given Id";
    private static final String NOT_FOUND_COMPANY = "Company not found with given Id";

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private MediaRepository mediaRepository;


    @Autowired
    private CompanyEntityMapper mapper;


    @Override
    public List<CompanyDTO> getAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(CompanyEntity -> mapper.toDTO(CompanyEntity)
                )
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CompanyEntity> getCompanyById(String id) {
        return companyRepository.findById(id);
    }


    @Override
    public void createCompany(CompanyDTO dto) {
        CompanyEntity entity = mapper.toEntity(dto);


        CompanySizeEntity sizeEntity = new CompanySizeEntity();
        sizeEntity.setId(dto.getSizeId());

        List<SubCategoryEntity> subCategoryEntities = dto.getSubCategoryDTOs().stream()
                .map(SubCategoryDTO::getId)
                .map(SubCategoryEntity::new)
                .collect(Collectors.toList());

        List<BenefitEntity> benefitEntities = dto.getBenefitDTOs().stream()
                .map(BenefitDTO::getId)
                .map(BenefitEntity::new)
                .collect(Collectors.toList());

        List<MediaEntity> mediaEntities = dto.getMediaDTOS().stream()
                .map(mediaDTO -> {
                    MediaEntity media = new MediaEntity();
                    media.setUrl(mediaDTO.getUrl());
                    return mediaRepository.save(media);
                })
                .collect(Collectors.toList());


        entity.setCompanySize(sizeEntity);
        entity.setCompanyBenefits(benefitEntities);
        entity.setCompanySubCategory(subCategoryEntities);
        entity.setMedias(mediaEntities);
        companyRepository.save(entity);

    }

    @Transactional
    @Override
    public CompanyEntity updateCompany(CompanyDTO dto) {

        CompanyEntity entity = mapper.toEntity(dto);

        CompanySizeEntity sizeEntity = new CompanySizeEntity();
        sizeEntity.setId(dto.getSizeId());

        List<SubCategoryEntity> subCategoryEntities = dto.getSubCategoryDTOs().stream()
                .map(SubCategoryDTO::getId)
                .map(SubCategoryEntity::new)
                .collect(Collectors.toList());

        List<BenefitEntity> benefitEntities = dto.getBenefitDTOs().stream()
                .map(BenefitDTO::getId)
                .map(BenefitEntity::new)
                .collect(Collectors.toList());

        List<MediaEntity> mediaEntities = dto.getMediaDTOS().stream()
                .map(mediaDTO -> {
                    MediaEntity media = new MediaEntity();
                    media.setUrl(mediaDTO.getUrl());
                    return mediaRepository.save(media);
                })
                .collect(Collectors.toList());

        entity.setCompanySize(sizeEntity);
        entity.setCompanyBenefits(benefitEntities);
        entity.setCompanySubCategory(subCategoryEntities);
        entity.setMedias(mediaEntities);
        return companyRepository.save(entity);
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
    public CompanyEntity findByTaxId(String taxId) {
        Optional<CompanyEntity> result = companyRepository.findByTaxId(taxId);
        return result.isPresent() ? result.get() : null;
    }

    @Override
    public Integer getCountByEmail(String email) {
        return companyRepository.countByEmail(email);
    }

    @Override
    public Integer getCountByTaxId(String taxId) {
        return companyRepository.countByTaxId(taxId);
    }

    @Override
    public Integer getCountById(String id) {
        return companyRepository.countById(id);
    }

    @Override
    public Optional<CompanyEntity> findCompanyById(String id) {
        return companyRepository.findById(id);
    }


}
