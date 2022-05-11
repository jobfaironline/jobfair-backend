package org.capstone.job_fair.services.mappers.job_fair;

import org.capstone.job_fair.controllers.payload.requests.job_fair.DraftJobFairRequest;
import org.capstone.job_fair.controllers.payload.requests.job_fair.UpdateJobFairRequest;
import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.services.mappers.company.CompanyMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {
        CompanyMapper.class
})
public abstract class JobFairMapper {
    @Mapping(target = "jobFairBoothList", ignore = true)
    public abstract JobFairEntity toEntity(JobFairDTO dto);

    public abstract JobFairDTO toDTO(JobFairEntity entity);

    @Mapping(target = "jobFairBoothList", ignore = true)
    public abstract void updateFromDTO(@MappingTarget JobFairEntity entity, JobFairDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "thumbnailUrl", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "cancelReason", ignore = true)
    @Mapping(target = "company", ignore = true)
    public abstract JobFairDTO toDTO(DraftJobFairRequest request);

    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "thumbnailUrl", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "cancelReason", ignore = true)
    @Mapping(target = "company", ignore = true)
    public abstract JobFairDTO toDTO(UpdateJobFairRequest request);

}
