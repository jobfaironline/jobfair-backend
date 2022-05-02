package org.capstone.job_fair.services.mappers.attendant.application;

import org.capstone.job_fair.models.dtos.attendant.application.ApplicationCertificationDTO;
import org.capstone.job_fair.models.entities.attendant.application.ApplicationCertificationEntity;
import org.capstone.job_fair.models.entities.attendant.cv.CvCertificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ApplicationCertificationMapper {
    public abstract ApplicationCertificationDTO toDTO(ApplicationCertificationEntity entity);

    public abstract ApplicationCertificationEntity toEntity(ApplicationCertificationDTO applicationCertificationDTO);

    @Mapping(target = "id", source = "id", ignore = true)
    public abstract ApplicationCertificationEntity toEntity(CvCertificationEntity cvCertification);
}
