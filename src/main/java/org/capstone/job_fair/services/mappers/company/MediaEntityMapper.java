package org.capstone.job_fair.services.mappers.company;

import org.capstone.job_fair.models.dtos.company.CompanyMediaDTO;
import org.capstone.job_fair.models.entities.company.CompanyMediaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {CompanyMapper.class}
)
public abstract class MediaEntityMapper {
    public abstract CompanyMediaEntity toEntity(CompanyMediaDTO dto);

    public abstract CompanyMediaDTO toDTO(CompanyMediaEntity entity);
}
