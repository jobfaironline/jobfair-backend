package org.capstone.job_fair.services.mappers.company;

import org.capstone.job_fair.models.dtos.company.JobFairBoothDTO;
import org.capstone.job_fair.models.entities.company.JobFairBoothEntity;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.services.mappers.job_fair.BoothMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {BoothMapper.class, BoothJobPositionMapper.class}
)
public abstract class JobFairBoothMapper {
    public abstract JobFairBoothDTO toDTO(JobFairBoothEntity entity);

    public abstract JobFairBoothEntity toEntity(JobFairBoothDTO dto);

    public abstract void updateEntity(JobFairBoothDTO dto, @MappingTarget JobFairBoothEntity entity);
}
