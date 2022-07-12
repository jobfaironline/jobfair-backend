package org.capstone.job_fair.services.mappers.attendant.profile;

import org.capstone.job_fair.controllers.payload.requests.attendant.UpdateAttendantRequest;
import org.capstone.job_fair.models.dtos.attendant.profile.ActivityDTO;
import org.capstone.job_fair.models.entities.attendant.profile.ActivityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ActivityMapper {
    public abstract ActivityEntity toEntity(ActivityDTO dto);

    public abstract ActivityDTO toDTO(ActivityEntity entity);

    @Mapping(target = "descriptionKeyWord", ignore = true)
    public abstract ActivityDTO toDTO(UpdateAttendantRequest.Activities request);

    public abstract void updateActivityEntityFromActivityDTO(ActivityDTO dto, @MappingTarget ActivityEntity entity);
}
