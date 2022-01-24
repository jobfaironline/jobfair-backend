package org.capstone.job_fair.services.mappers;


import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.CreateCompanyRequest;
import org.capstone.job_fair.controllers.payload.requests.UpdateCompanyRequest;
import org.capstone.job_fair.models.dtos.company.*;
import org.capstone.job_fair.models.entities.attendant.cv.SkillEntity;
import org.capstone.job_fair.models.entities.company.*;
import org.capstone.job_fair.utils.MessageUtil;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.NoSuchElementException;
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


    @Mapping(target = "subCategories", source = "subCategoryDTOs", qualifiedByName = "fromSubCategoryDTOsOfCompanyDTO")
    @Mapping(target = "companyBenefits", source = "benefitDTOs", qualifiedByName = "fromBenefitDTOsFromCompanyDTO")
    @Mapping(target = "medias", source = "mediaDTOS", qualifiedByName = "fromMediaDTOsOfCompanyDTO")
    @Mapping(target = "companySize", source = "sizeId", qualifiedByName = "fromSizeIdOfCompanyDTO")
    public abstract CompanyEntity toEntity(CompanyDTO dto);

    @Mapping(target = "subCategoryDTOs", source = "subCategories", qualifiedByName = "fromSubCategoriesOfCompanyEntity")
    @Mapping(target = "benefitDTOs", source = "companyBenefits", qualifiedByName = "fromCompanyBenefitsOfCompanyEntity")
    @Mapping(target = "mediaDTOS", source = "medias", qualifiedByName = "fromMediasOfCompanyEntity")
    @Mapping(target = "sizeId", source = "companySize", qualifiedByName = "fromCompanySizeOfCompanyEntity")
    public abstract CompanyDTO toDTO(CompanyEntity entity);

    @Mapping(source = "url", target = "websiteUrl")
    @Mapping(source = "mediaUrls", target = "mediaDTOS", qualifiedByName = "fromMediaUrlsOfCreateCompanyRequest")
    @Mapping(source = "benefitIds", target = "benefitDTOs", qualifiedByName = "fromBenefitIdsOfCreateCompanyRequest")
    @Mapping(source = "subCategoriesIds", target = "subCategoryDTOs", qualifiedByName = "fromSubCategoriesIdsOfCreateCompanyRequest")
    @Mapping(source = "taxId", target = "taxId")
    public abstract CompanyDTO toDTO(CreateCompanyRequest request);

    @Mapping(source = "url", target = "websiteUrl")
    @Mapping(source = "mediaUrls", target = "mediaDTOS", qualifiedByName = "fromMediaUrlsOfCreateCompanyRequest")
    @Mapping(source = "benefitIds", target = "benefitDTOs", qualifiedByName = "fromBenefitIdsOfCreateCompanyRequest")
    @Mapping(source = "subCategoriesIds", target = "subCategoryDTOs", qualifiedByName = "fromSubCategoriesIdsOfCreateCompanyRequest")
    @Mapping(source = "taxId", target = "taxId")
    public abstract CompanyDTO toDTO(UpdateCompanyRequest request);


    @Mapping(target = "subCategories", source = "subCategoryDTOs", qualifiedByName = "updateSubCategoriesOfCompanyEntity")
    @Mapping(target = "companyBenefits", source = "benefitDTOs", qualifiedByName = "fromBenefitDTOsFromCompanyDTO")
    @Mapping(target = "medias", source = "mediaDTOS", qualifiedByName = "fromMediaDTOsOfCompanyDTO")
    @Mapping(target = "companySize", source = "sizeId", qualifiedByName = "fromSizeIdOfCompanyDTO")
    public abstract void updateCompanyEntity(CompanyDTO dto, @MappingTarget CompanyEntity entity);

    @Named("updateSubCategoriesOfCompanyEntity")
    public void updateSubCategoriesOfCompanyEntity(Set<SubCategoryDTO> dtos, @MappingTarget Set<SubCategoryEntity> entities){
        if (dtos == null) return;
        dtos.forEach(subCategoryDTO -> {
            if (entities.stream().anyMatch(entity -> entity.getId().equals(subCategoryDTO.getId()))){
                return;
            }
            SubCategoryEntity entity = new SubCategoryEntity();
            entity.setId(subCategoryDTO.getId());
            entities.add(entity);
        });
        entities.removeIf(entity -> {
            return dtos.stream().noneMatch(subCategoryDTO -> entity.getId().equals(subCategoryDTO.getId()));
        });
    }


    @Named("fromMediaUrlsOfCreateCompanyRequest")
    public List<MediaDTO> fromMediaUrlsOfCreateCompanyRequest(List<String> mediaUrls){
        if (mediaUrls == null) return null;
        return mediaUrls.stream().map(MediaDTO::new).collect(Collectors.toList());
    }

    @Named("fromBenefitIdsOfCreateCompanyRequest")
    public Set<BenefitDTO> fromBenefitIdsOfCreateCompanyRequest(List<Integer> benefitIds){
        if (benefitIds == null) return null;
        return benefitIds.stream().map(BenefitDTO::new).collect(Collectors.toSet());
    }

    @Named("fromSubCategoriesIdsOfCreateCompanyRequest")
    public Set<SubCategoryDTO> fromSubCategoriesIdsOfCreateCompanyRequest(List<Integer> subCategoriesIds){
        if (subCategoriesIds == null) return null;
        return subCategoriesIds.stream().map(SubCategoryDTO::new).collect(Collectors.toSet());    }

    @Named("fromSubCategoriesOfCompanyEntity")
    public Set<SubCategoryDTO> fromSubCategoriesOfCompanyEntity(Set<SubCategoryEntity> entities) {
        if (entities == null) return null;
        return entities.stream().map(entity -> subCategoryMapper.toDTO(entity)).collect(Collectors.toSet());
    }

    @Named("fromCompanyBenefitsOfCompanyEntity")
    public Set<BenefitDTO> fromCompanyBenefitsOfCompanyEntity(Set<BenefitEntity> entities) {
        if (entities == null) return null;
        return entities.stream().map(entity -> benefitMapper.toDTO(entity)).collect(Collectors.toSet());
    }

    @Named("fromMediasOfCompanyEntity")
    public List<MediaDTO> fromMediasOfCompanyEntity(List<MediaEntity> entities){
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

    @Named("fromBenefitDTOsFromCompanyDTO")
    public Set<BenefitEntity> fromBenefitDTOsFromCompanyDTO(Set<BenefitDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> benefitMapper.toEntity(dto)).collect(Collectors.toSet());
    }

    @Named("fromMediaDTOsOfCompanyDTO")
    public List<MediaEntity> fromMediaDTOsOfCompanyDTO(List<MediaDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> mediaMapper.toEntity(dto)).collect(Collectors.toList());
    }

    @Named("fromSizeIdOfCompanyDTO")
    public CompanySizeEntity fromSizeIdOfCompanyDTO(Integer sizeId){
        if (sizeId == null) return null;
        CompanySizeEntity companySize = new CompanySizeEntity();
        companySize.setId(sizeId);
        return companySize;
    }
}
