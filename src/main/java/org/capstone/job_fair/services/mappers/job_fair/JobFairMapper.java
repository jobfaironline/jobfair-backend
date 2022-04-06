package org.capstone.job_fair.services.mappers.job_fair;

import org.capstone.job_fair.controllers.payload.requests.job_fair.DraftJobFairRequest;
import org.capstone.job_fair.controllers.payload.requests.job_fair.UpdateJobFairRequest;
import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class JobFairMapper {
    public abstract JobFairEntity toEntity(JobFairDTO dto);

    public abstract JobFairDTO toDTO(JobFairEntity entity);

    public abstract void updateFromDTO(@MappingTarget JobFairEntity entity, JobFairDTO dto);

    public abstract JobFairDTO toDTO(DraftJobFairRequest request);

    public abstract JobFairDTO toDTO(UpdateJobFairRequest request);

}
