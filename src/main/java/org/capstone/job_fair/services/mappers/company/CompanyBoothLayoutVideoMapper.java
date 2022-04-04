package org.capstone.job_fair.services.mappers.company;

import org.capstone.job_fair.models.dtos.company.CompanyBoothLayoutVideoDTO;
import org.capstone.job_fair.models.entities.company.JobFairBoothLayoutVideoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class CompanyBoothLayoutVideoMapper {
    public abstract JobFairBoothLayoutVideoEntity toEntity(CompanyBoothLayoutVideoDTO dto);

    public abstract CompanyBoothLayoutVideoDTO toDTO(JobFairBoothLayoutVideoEntity entity);
}
