package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.company.job.JobPositionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
public abstract class JobPositionEntityMapper {
    @Mapping(target = "language", ignore = true)
    @Mapping(target = "level", ignore = true)
    @Mapping(target = "jobType", ignore = true)
    public abstract JobPositionDTO toDTO(JobPositionEntity jobPosition);
    @Mapping(target = "language", ignore = true)
    @Mapping(target = "jobLevel", ignore = true)
    @Mapping(target = "jobTypeEntity", ignore = true)
    public abstract JobPositionEntity toEntity(JobPositionDTO jobPositionDTO);
}
