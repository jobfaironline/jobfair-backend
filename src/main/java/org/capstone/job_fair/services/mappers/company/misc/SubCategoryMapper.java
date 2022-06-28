package org.capstone.job_fair.services.mappers.company.misc;

import org.capstone.job_fair.models.dtos.company.misc.SubCategoryDTO;
import org.capstone.job_fair.models.entities.company.misc.SubCategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {ProfessionCategoryEntityMapper.class})
public abstract class SubCategoryMapper {
    public abstract SubCategoryDTO toDTO(SubCategoryEntity entity);

    public abstract SubCategoryEntity toEntity(SubCategoryDTO dto);
}
