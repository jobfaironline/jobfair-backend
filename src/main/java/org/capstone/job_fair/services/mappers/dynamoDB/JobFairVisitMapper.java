package org.capstone.job_fair.services.mappers.dynamoDB;

import org.capstone.job_fair.models.dtos.dynamoDB.JobFairVisitDTO;
import org.capstone.job_fair.models.entities.dynamoDB.JobFairVisitEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class JobFairVisitMapper {
    public abstract JobFairVisitEntity toEntity(JobFairVisitDTO dto);

    public abstract JobFairVisitDTO toDTO(JobFairVisitEntity entity);
}
