package org.capstone.job_fair.services.mappers;


import org.capstone.job_fair.controllers.payload.requests.UpdateAttendantRequest;
import org.capstone.job_fair.models.dtos.attendant.cv.WorkHistoryDTO;
import org.capstone.job_fair.models.entities.attendant.cv.WorkHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class WorkHistoryMapper {

    public abstract WorkHistoryEntity toEntity(WorkHistoryDTO dto);

    public abstract WorkHistoryDTO toDTO(WorkHistoryEntity entity);

    public abstract WorkHistoryDTO toDTO(UpdateAttendantRequest.WorkHistories request);

    public abstract void updateWorkHistoryEntityFromWorkHistoryDTO(WorkHistoryDTO dto, @MappingTarget WorkHistoryEntity entity);
}
