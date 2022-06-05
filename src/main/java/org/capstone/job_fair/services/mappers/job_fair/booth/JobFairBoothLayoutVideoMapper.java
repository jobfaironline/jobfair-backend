package org.capstone.job_fair.services.mappers.job_fair.booth;

import org.capstone.job_fair.models.dtos.job_fair.booth.JobFairBoothLayoutVideoDTO;
import org.capstone.job_fair.models.entities.job_fair.booth.JobFairBoothLayoutVideoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class JobFairBoothLayoutVideoMapper {
    public abstract JobFairBoothLayoutVideoEntity toEntity(JobFairBoothLayoutVideoDTO dto);

    public abstract JobFairBoothLayoutVideoDTO toDTO(JobFairBoothLayoutVideoEntity entity);
}
