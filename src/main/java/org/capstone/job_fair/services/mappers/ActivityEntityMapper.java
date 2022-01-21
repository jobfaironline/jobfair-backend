package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.models.dtos.attendant.cv.ActivityDTO;
import org.capstone.job_fair.models.entities.attendant.cv.ActivityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ActivityEntityMapper {
    public abstract ActivityEntity toEntity(ActivityDTO dto);
}
