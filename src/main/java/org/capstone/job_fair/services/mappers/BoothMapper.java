package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.models.dtos.job_fair.BoothDTO;
import org.capstone.job_fair.models.entities.job_fair.BoothEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class BoothMapper {
    @Mapping(target = "layout", ignore = true)
    public abstract BoothDTO toDTO(BoothEntity entity);

    @Mapping(target = "layout", ignore = true)
    public abstract BoothEntity toEntity(BoothDTO dto);

}
