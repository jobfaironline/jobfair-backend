package org.capstone.job_fair.services.mappers.attendant.application;

import org.capstone.job_fair.models.dtos.attendant.application.ApplicationEducationDTO;
import org.capstone.job_fair.models.entities.attendant.application.ApplicationEducationEntity;
import org.capstone.job_fair.models.entities.attendant.cv.CvEducationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ApplicationEducationMapper {
    public abstract ApplicationEducationDTO toDTO(ApplicationEducationEntity entity);

    @Mapping(target = "application", ignore = true)
    public abstract ApplicationEducationEntity toEntity(ApplicationEducationDTO dto);

    @Mapping(target = "application", ignore = true)
    @Mapping(target = "id", source = "id", ignore = true)
    public abstract ApplicationEducationEntity toEntity(CvEducationEntity cvCertification);
}
