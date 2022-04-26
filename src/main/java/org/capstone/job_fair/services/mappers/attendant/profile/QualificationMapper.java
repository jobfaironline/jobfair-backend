package org.capstone.job_fair.services.mappers.attendant.profile;

import org.capstone.job_fair.models.dtos.attendant.profile.QualificationDTO;
import org.capstone.job_fair.models.entities.attendant.profile.QualificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class QualificationMapper {
    public abstract QualificationDTO toDTO(QualificationEntity entity);

    public abstract QualificationEntity toEntity(QualificationDTO dto);
}
