package org.capstone.job_fair.services.mappers.company;

import org.capstone.job_fair.models.dtos.company.JobFairBoothLayoutVideoDTO;
import org.capstone.job_fair.models.entities.company.JobFairBoothLayoutVideoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class CompanyBoothLayoutVideoMapper {
    public abstract JobFairBoothLayoutVideoEntity toEntity(JobFairBoothLayoutVideoDTO dto);

    public abstract JobFairBoothLayoutVideoDTO toDTO(JobFairBoothLayoutVideoEntity entity);
}
