package org.capstone.job_fair.services.mappers.attendant.application;

import org.capstone.job_fair.models.dtos.attendant.application.ApplicationActivityDTO;
import org.capstone.job_fair.models.entities.attendant.application.ApplicationActivityEntity;
import org.capstone.job_fair.models.entities.attendant.cv.CvActivityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ApplicationActivityMapper {
    public abstract ApplicationActivityDTO toDTO(ApplicationActivityEntity entity);

    public abstract ApplicationActivityEntity toEntity(ApplicationActivityDTO dto);

    @Mapping(target = "id", source = "id", ignore = true)
    public abstract ApplicationActivityEntity toEntity(CvActivityEntity cvActivityEntity);
}
