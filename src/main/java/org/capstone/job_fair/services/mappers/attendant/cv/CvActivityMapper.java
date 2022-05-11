package org.capstone.job_fair.services.mappers.attendant.cv;

import org.capstone.job_fair.controllers.payload.requests.attendant.DraftCvRequest;
import org.capstone.job_fair.models.dtos.attendant.cv.CvActivityDTO;
import org.capstone.job_fair.models.entities.attendant.cv.CvActivityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CvActivityMapper {
    public abstract CvActivityDTO toDTO(CvActivityEntity entity);

    @Mapping(target = "cv", ignore = true)
    public abstract CvActivityEntity toEntity(CvActivityDTO dto);

    @Mapping(target = "id", ignore = true)
    public abstract CvActivityDTO toDTO(DraftCvRequest.Activities request);
}
