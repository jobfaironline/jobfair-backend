package org.capstone.job_fair.services.mappers.attendant.application;

import org.capstone.job_fair.models.dtos.attendant.application.ApplicationReferenceDTO;
import org.capstone.job_fair.models.entities.attendant.application.ApplicationReferenceEntity;
import org.capstone.job_fair.models.entities.attendant.cv.CvReferenceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ApplicationReferenceMapper {
    @Mapping(target = "application", ignore = true)
    public abstract ApplicationReferenceEntity toEntity(ApplicationReferenceDTO dto);

    public abstract ApplicationReferenceDTO toDTO(ApplicationReferenceEntity entity);

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "application", ignore = true)
    public abstract ApplicationReferenceEntity toEntity(CvReferenceEntity cvReferenceEntity);
}
