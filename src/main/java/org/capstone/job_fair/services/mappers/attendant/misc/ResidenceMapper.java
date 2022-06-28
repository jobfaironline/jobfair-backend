package org.capstone.job_fair.services.mappers.attendant.misc;

import org.capstone.job_fair.models.dtos.attendant.misc.ResidenceDTO;
import org.capstone.job_fair.models.entities.attendant.misc.ResidenceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ResidenceMapper {
    public abstract ResidenceEntity toEntity(ResidenceDTO dto);

    public abstract ResidenceDTO toDTO(ResidenceEntity entity);

}
