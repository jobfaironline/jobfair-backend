package org.capstone.job_fair.services.mappers.job_fair;

import org.capstone.job_fair.models.dtos.job_fair.AttendantJobFairStatusDTO;
import org.capstone.job_fair.models.entities.job_fair.AttendantJobFairStatusEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {JobFairMapper.class}
)
public abstract class AttendantJobFairStatusMapper {
    public abstract AttendantJobFairStatusDTO toDTO(AttendantJobFairStatusEntity entity);
    public abstract AttendantJobFairStatusEntity toEntity(AttendantJobFairStatusDTO dto);
}