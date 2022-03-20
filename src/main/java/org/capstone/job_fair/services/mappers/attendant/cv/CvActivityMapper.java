package org.capstone.job_fair.services.mappers.attendant.cv;

import org.capstone.job_fair.models.dtos.attendant.cv.CvActivityDTO;
import org.capstone.job_fair.models.entities.attendant.cv.CvActivityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CvActivityMapper {
    public abstract CvActivityDTO toDTO(CvActivityEntity entity);

    public abstract CvActivityEntity toEntity(CvActivityDTO dto);
}
