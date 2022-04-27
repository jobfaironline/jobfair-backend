package org.capstone.job_fair.services.mappers.attendant;


import org.capstone.job_fair.models.dtos.attendant.JobLevelDTO;
import org.capstone.job_fair.models.entities.attendant.JobLevelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class JobLevelMapper {
    public abstract JobLevelDTO toDTO(JobLevelEntity entity);
    public abstract JobLevelEntity toEntity(JobLevelDTO dto);
}
