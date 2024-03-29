package org.capstone.job_fair.services.mappers.company.misc;

import org.capstone.job_fair.models.dtos.company.misc.ProfessionCategoryDTO;
import org.capstone.job_fair.models.entities.company.misc.ProfessionCategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ProfessionCategoryEntityMapper {
    public abstract ProfessionCategoryEntity toEntity(ProfessionCategoryDTO dto);

    public abstract ProfessionCategoryDTO toDTO(ProfessionCategoryEntity dto);
}
