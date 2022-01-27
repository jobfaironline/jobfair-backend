package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface JobFairMapper {
    public JobFairEntity toJobFairEntity(JobFairDTO dto);
    public JobFairDTO toJobFairDTO(JobFairEntity entity);
}
