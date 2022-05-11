package org.capstone.job_fair.services.mappers.job_fair.booth;

import org.capstone.job_fair.models.dtos.job_fair.booth.JobFairBoothDTO;
import org.capstone.job_fair.models.entities.job_fair.booth.JobFairBoothEntity;
import org.capstone.job_fair.services.mappers.job_fair.JobFairMapper;
import org.capstone.job_fair.services.mappers.job_fair.LayoutBoothMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {LayoutBoothMapper.class, BoothJobPositionMapper.class, JobFairMapper.class}
)
public abstract class JobFairBoothMapper {
    public abstract JobFairBoothDTO toDTO(JobFairBoothEntity entity);

    @Mapping(target = "assignments", ignore = true)
    public abstract JobFairBoothEntity toEntity(JobFairBoothDTO dto);

    @Mapping(target = "assignments", ignore = true)
    public abstract void updateEntity(JobFairBoothDTO dto, @MappingTarget JobFairBoothEntity entity);
}
