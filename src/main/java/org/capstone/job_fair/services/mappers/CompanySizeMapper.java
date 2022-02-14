package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.models.dtos.company.CompanySizeDTO;
import org.capstone.job_fair.models.entities.company.CompanySizeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CompanySizeMapper {
    public abstract CompanySizeEntity toEntity(CompanySizeDTO dto);

    public abstract CompanySizeDTO toDTO(CompanySizeEntity entity);
}
