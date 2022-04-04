package org.capstone.job_fair.services.mappers.job_fair;

import org.capstone.job_fair.models.dtos.job_fair.BoothDTO;
import org.capstone.job_fair.models.entities.job_fair.LayoutBoothEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class BoothMapper {
    @Mapping(target = "layout", ignore = true)
    public abstract BoothDTO toDTO(LayoutBoothEntity entity);

    @Mapping(target = "layout", ignore = true)
    public abstract LayoutBoothEntity toEntity(BoothDTO dto);

}
