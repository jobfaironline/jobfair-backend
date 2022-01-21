package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.models.dtos.attendant.cv.ReferenceDTO;
import org.capstone.job_fair.models.entities.attendant.cv.ReferenceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ReferenceEntityMapper {
    public abstract ReferenceEntity toEntity(ReferenceDTO dto);
}
