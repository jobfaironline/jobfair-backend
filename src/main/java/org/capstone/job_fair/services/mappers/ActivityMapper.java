package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.controllers.payload.requests.UpdateAttendantRequest;
import org.capstone.job_fair.models.dtos.attendant.cv.ActivityDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.SkillDTO;
import org.capstone.job_fair.models.entities.attendant.cv.ActivityEntity;
import org.capstone.job_fair.models.entities.attendant.cv.SkillEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ActivityMapper {
    public abstract ActivityEntity toEntity(ActivityDTO dto);
    public abstract ActivityDTO toDTO(ActivityEntity entity);
    public abstract ActivityDTO toDTO(UpdateAttendantRequest.Activities request);

    public abstract void updateActivityEntityFromActivityDTO(ActivityDTO dto, @MappingTarget ActivityEntity entity);
}
