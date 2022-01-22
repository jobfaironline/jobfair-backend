package org.capstone.job_fair.services.mappers;


import org.capstone.job_fair.models.dtos.company.*;
import org.capstone.job_fair.models.entities.company.*;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", uses = {SubCategoryMapper.class, BenefitEntityMapper.class, MediaEntityMapper.class})
public abstract class CompanyEntityMapper {

    @Autowired
    private SubCategoryMapper subCategoryMapper;

    @Autowired
    private BenefitEntityMapper benefitMapper;

    @Autowired
    private MediaEntityMapper mediaMapper;


    @Mapping(target = "companySubCategory", source = "subCategoryDTOs", qualifiedByName = "subCategoryDTOToEntity")
    @Mapping(target = "companyBenefits", source = "benefitDTOs", qualifiedByName = "companyBenefitDTOToEntity")
    @Mapping(target = "medias", source = "mediaDTOS", qualifiedByName = "mediaDTOToEntity")
    public abstract CompanyEntity toEntity(CompanyDTO dto);

    @Mapping(target = "subCategoryDTOs", source = "companySubCategory", qualifiedByName = "subCategoryEntityToDTO")
    @Mapping(target = "benefitDTOs", source = "companyBenefits", qualifiedByName = "companyBenefitEntityToDTO")
    @Mapping(target = "mediaDTOS", source = "medias", qualifiedByName = "mediaEntityToDTO")
    @Mapping(target = "sizeId", source = "companySize", qualifiedByName = "sizeEntityToSizeId")
    public abstract CompanyDTO toDTO(CompanyEntity entity);

/////////////////
    @Named("subCategoryEntityToDTO")
    public List<SubCategoryDTO> subCategoryEntityToDTO(List<SubCategoryEntity> entities) {
        if (entities == null) return null;
        return entities.stream().map(entity -> subCategoryMapper.toDTO(entity)).collect(Collectors.toList());
    }

    @Named("companyBenefitEntityToDTO")
    public List<BenefitDTO> companyBenefitEntityToDTO(List<BenefitEntity> entities) {
        if (entities == null) return null;
        return entities.stream().map(entity -> benefitMapper.toDTO(entity)).collect(Collectors.toList());
    }

    @Named("mediaEntityToDTO")
    public List<MediaDTO> mediaEntityToDTO(List<MediaEntity> entities){
        return entities.stream().map(entity -> mediaMapper.toDTO(entity)).collect(Collectors.toList());
    }

    @Named("sizeEntityToSizeId")
    public Integer sizeEntityToSizeId(CompanySizeEntity entity) {
        if (entity == null) return null;
        return entity.getId();
    }
////////////////
    @Named("subCategoryDTOToEntity")
    public List<SubCategoryEntity> subCategoryDTOToEntity(List<SubCategoryDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> subCategoryMapper.toEntity(dto)).collect(Collectors.toList());
    }

    @Named("companyBenefitDTOToEntity")
    public List<BenefitEntity> companyBenefitDTOToEntity(List<BenefitDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> benefitMapper.toEntity(dto)).collect(Collectors.toList());
    }

    @Named("mediaDTOToEntity")
    public List<MediaEntity> mediaDTOToEntity(List<MediaDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> mediaMapper.toEntity(dto)).collect(Collectors.toList());
    }

}
