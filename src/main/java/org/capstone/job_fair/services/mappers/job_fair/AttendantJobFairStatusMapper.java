package org.capstone.job_fair.services.mappers.job_fair;

import org.capstone.job_fair.controllers.payload.responses.JobFairForAttendantResponse;
import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {JobFairMapper.class}
)
public abstract class AttendantJobFairStatusMapper {
    public abstract AttendantJobFairStatusDTO toDTO(AttendantJobFairStatusEntity entity);

    public abstract AttendantJobFairStatusEntity toEntity(AttendantJobFairStatusDTO dto);

    @Mapping(target = "status", ignore = true)
    public abstract JobFairForAttendantResponse toJobFairForAttendantResponse(JobFairDTO dto);

}
