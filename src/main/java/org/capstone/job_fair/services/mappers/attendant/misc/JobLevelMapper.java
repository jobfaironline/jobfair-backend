package org.capstone.job_fair.services.mappers.attendant.misc;


import org.capstone.job_fair.models.dtos.attendant.misc.JobLevelDTO;
import org.capstone.job_fair.models.entities.attendant.misc.JobLevelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class JobLevelMapper {
    public abstract JobLevelDTO toDTO(JobLevelEntity entity);

    public abstract JobLevelEntity toEntity(JobLevelDTO dto);
}
