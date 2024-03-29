package org.capstone.job_fair.services.mappers.attendant.profile;


import org.capstone.job_fair.controllers.payload.requests.attendant.UpdateAttendantRequest;
import org.capstone.job_fair.models.dtos.attendant.profile.WorkHistoryDTO;
import org.capstone.job_fair.models.entities.attendant.profile.WorkHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class WorkHistoryMapper {

    public abstract WorkHistoryEntity toEntity(WorkHistoryDTO dto);

    public abstract WorkHistoryDTO toDTO(WorkHistoryEntity entity);

    @Mapping(target = "descriptionKeyWord", ignore = true)
    public abstract WorkHistoryDTO toDTO(UpdateAttendantRequest.WorkHistories request);

    public abstract void updateWorkHistoryEntityFromWorkHistoryDTO(WorkHistoryDTO dto, @MappingTarget WorkHistoryEntity entity);
}
