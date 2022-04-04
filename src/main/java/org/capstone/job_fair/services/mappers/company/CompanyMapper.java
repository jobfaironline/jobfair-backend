package org.capstone.job_fair.services.mappers.company;


import org.capstone.job_fair.controllers.payload.requests.company.CreateCompanyRequest;
import org.capstone.job_fair.controllers.payload.requests.company.UpdateCompanyRequest;
import org.capstone.job_fair.models.dtos.company.*;
import org.capstone.job_fair.models.entities.company.*;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", uses = {SubCategoryMapper.class, BenefitEntityMapper.class, MediaEntityMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CompanyMapper {

    @Autowired
    private SubCategoryMapper subCategoryMapper;

    @Autowired
    private BenefitEntityMapper benefitMapper;

    @Autowired
    private MediaEntityMapper mediaMapper;

    @Autowired
    private CompanyBenefitMapper companyBenefitMapper;


    @Mapping(target = "subCategories", source = "subCategoryDTOs", qualifiedByName = "fromSubCategoryDTOsOfCompanyDTO")
    @Mapping(target = "companyBenefits", source = "companyBenefitDTOS", qualifiedByName = "fromCompanyBenefitsOfCreateCompanyRequest")
    @Mapping(target = "medias", source = "mediaDTOS", qualifiedByName = "fromMediaDTOsOfCompanyDTO")
    @Mapping(target = "companySize", source = "sizeId", qualifiedByName = "fromSizeIdOfCompanyDTO")
    public abstract CompanyEntity toEntity(CompanyDTO dto);


    @Mapping(target = "subCategoryDTOs", source = "subCategories", qualifiedByName = "fromSubCategoriesOfCompanyEntity")
    @Mapping(target = "companyBenefitDTOS", source = "companyBenefits", qualifiedByName = "fromCompanyBenefitsOfCompanyEntity")
    @Mapping(target = "mediaDTOS", source = "medias", qualifiedByName = "fromMediasOfCompanyEntity")
    @Mapping(target = "sizeId", source = "companySize", qualifiedByName = "fromCompanySizeOfCompanyEntity")
    public abstract CompanyDTO toDTO(CompanyEntity entity);

    @Mapping(source = "url", target = "websiteUrl")
    @Mapping(source = "mediaUrls", target = "mediaDTOS", qualifiedByName = "fromMediaUrlsOfCreateCompanyRequest")
    @Mapping(source = "benefits", target = "companyBenefitDTOS", qualifiedByName = "fromBenefitsOfCreateCompanyRequest")
    @Mapping(source = "subCategoriesIds", target = "subCategoryDTOs", qualifiedByName = "fromSubCategoriesIdsOfCreateCompanyRequest")
    @Mapping(source = "taxId", target = "taxId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employeeMaxNum", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "companyLogoURL", ignore = true)
    public abstract CompanyDTO toDTO(CreateCompanyRequest request);

    @Mapping(source = "url", target = "websiteUrl")
    @Mapping(source = "mediaUrls", target = "mediaDTOS", qualifiedByName = "fromMediaUrlsOfCreateCompanyRequest")
    @Mapping(source = "benefits", target = "companyBenefitDTOS", qualifiedByName = "fromBenefitsOfUpdateCompanyRequest")
    @Mapping(source = "subCategoriesIds", target = "subCategoryDTOs", qualifiedByName = "fromSubCategoriesIdsOfCreateCompanyRequest")
    @Mapping(source = "taxId", target = "taxId")
    @Mapping(target = "companyLogoURL", ignore = true)
    public abstract CompanyDTO toDTO(UpdateCompanyRequest request);


    @Mapping(target = "subCategories", source = "subCategoryDTOs", qualifiedByName = "updateSubCategoriesOfCompanyEntity")
    @Mapping(target = "companyBenefits", source = "companyBenefitDTOS", qualifiedByName = "updateCompanyBenefitsOfCompanyEntity")
    @Mapping(target = "medias", source = "mediaDTOS", qualifiedByName = "fromMediaDTOsOfCompanyDTO")
    @Mapping(target = "companySize", source = "sizeId", qualifiedByName = "fromSizeIdOfCompanyDTO")
    public abstract void updateCompanyEntity(CompanyDTO dto, @MappingTarget CompanyEntity entity);

    @Named("updateSubCategoriesOfCompanyEntity")
    public void updateSubCategoriesOfCompanyEntity(Set<SubCategoryDTO> dtos, @MappingTarget Set<SubCategoryEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(subCategoryDTO -> {
            if (entities.stream().anyMatch(entity -> entity.getId().equals(subCategoryDTO.getId()))) {
                return;
            }
            SubCategoryEntity entity = new SubCategoryEntity();
            entity.setId(subCategoryDTO.getId());
            entities.add(entity);
        });
        entities.removeIf(entity -> dtos.stream().noneMatch(subCategoryDTO -> entity.getId().equals(subCategoryDTO.getId())));
    }

    @Named("updateCompanyBenefitsOfCompanyEntity")
    public void updateCompanyBenefitsOfCompanyEntity(Set<CompanyBenefitDTO> dtos, @MappingTarget Set<CompanyBenefitEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(dto -> {

            Optional<CompanyBenefitEntity> opt = entities.stream().filter(entity -> entity.getBenefit().getId().equals(dto.getBenefitDTO().getId())).findFirst();
            if (opt.isPresent()) {
                companyBenefitMapper.updateCompanyBenefitEntity(dto, opt.get());
                return;
            }
            CompanyBenefitEntity entity = companyBenefitMapper.toEntity(dto);
            entities.add(entity);
        });
        entities.removeIf(entity -> dtos.stream().noneMatch(dto -> Objects.equals(entity.getBenefit().getId(),dto.getBenefitDTO().getId())));
    }

    @Named("fromBenefitsOfUpdateCompanyRequest")
    public Set<CompanyBenefitDTO> fromBenefitsOfUpdateCompanyRequest(List<UpdateCompanyRequest.BenefitRequest> benefits) {
        if (benefits == null) return null;
        return benefits.stream().map(benefitRequest -> {
            BenefitDTO benefitDTO = new BenefitDTO();
            benefitDTO.setId(benefitRequest.getId());
            return CompanyBenefitDTO.builder().description(benefitRequest.getDescription()).benefitDTO(benefitDTO).build();
        }).collect(Collectors.toSet());
    }


    @Named("fromMediaUrlsOfCreateCompanyRequest")
    public List<MediaDTO> fromMediaUrlsOfCreateCompanyRequest(List<String> mediaUrls) {
        if (mediaUrls == null) return null;
        return mediaUrls.stream().map(MediaDTO::new).collect(Collectors.toList());
    }

    @Named("fromBenefitsOfCreateCompanyRequest")
    public Set<CompanyBenefitDTO> fromBenefitsOfCreateCompanyRequest(List<CreateCompanyRequest.BenefitRequest> benefits) {
        if (benefits == null) return null;
        return benefits.stream().map(benefitRequest -> {
            BenefitDTO benefitDTO = new BenefitDTO();
            benefitDTO.setId(benefitRequest.getId());
            return CompanyBenefitDTO.builder().description(benefitRequest.getDescription()).benefitDTO(benefitDTO).build();
        }).collect(Collectors.toSet());
    }

    @Named("fromSubCategoriesIdsOfCreateCompanyRequest")
    public Set<SubCategoryDTO> fromSubCategoriesIdsOfCreateCompanyRequest(List<Integer> subCategoriesIds) {
        if (subCategoriesIds == null) return null;
        return subCategoriesIds.stream().map(SubCategoryDTO::new).collect(Collectors.toSet());
    }

    @Named("fromSubCategoriesOfCompanyEntity")
    public Set<SubCategoryDTO> fromSubCategoriesOfCompanyEntity(Set<SubCategoryEntity> entities) {
        if (entities == null) return null;
        return entities.stream().map(entity -> subCategoryMapper.toDTO(entity)).collect(Collectors.toSet());
    }

    @Named("fromCompanyBenefitsOfCompanyEntity")
    public Set<CompanyBenefitDTO> fromCompanyBenefitsOfCompanyEntity(Set<CompanyBenefitEntity> entities) {
        if (entities == null) return null;
        return entities.stream().map(entity -> companyBenefitMapper.toDTO(entity)).collect(Collectors.toSet());
    }

    @Named("fromMediasOfCompanyEntity")
    public List<MediaDTO> fromMediasOfCompanyEntity(List<CompanyMediaEntity> entities) {
        if (entities == null) return null;
        return entities.stream().map(entity -> mediaMapper.toDTO(entity)).collect(Collectors.toList());
    }

    @Named("fromCompanySizeOfCompanyEntity")
    public Integer fromCompanySizeOfCompanyEntity(CompanySizeEntity entity) {
        if (entity == null) return null;
        return entity.getId();
    }

    @Named("fromSubCategoryDTOsOfCompanyDTO")
    public Set<SubCategoryEntity> fromSubCategoryDTOsOfCompanyDTO(Set<SubCategoryDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> subCategoryMapper.toEntity(dto)).collect(Collectors.toSet());
    }

    @Named("fromCompanyBenefitsOfCreateCompanyRequest")
    public Set<CompanyBenefitEntity> fromCompanyBenefitsOfCreateCompanyRequest(Set<CompanyBenefitDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> companyBenefitMapper.toEntity(dto)).collect(Collectors.toSet());
    }

    @Named("fromMediaDTOsOfCompanyDTO")
    public List<CompanyMediaEntity> fromMediaDTOsOfCompanyDTO(List<MediaDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> mediaMapper.toEntity(dto)).collect(Collectors.toList());
    }

    @Named("fromSizeIdOfCompanyDTO")
    public CompanySizeEntity fromSizeIdOfCompanyDTO(Integer sizeId) {
        if (sizeId == null) return null;
        CompanySizeEntity companySize = new CompanySizeEntity();
        companySize.setId(sizeId);
        return companySize;
    }
}
