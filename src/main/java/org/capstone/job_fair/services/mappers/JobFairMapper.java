package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class JobFairMapper {
    public abstract JobFairEntity toJobFairEntity(JobFairDTO dto);

    public abstract JobFairDTO toJobFairDTO(JobFairEntity entity);
}
