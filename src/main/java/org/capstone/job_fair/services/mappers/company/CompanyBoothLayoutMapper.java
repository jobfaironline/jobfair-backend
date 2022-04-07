package org.capstone.job_fair.services.mappers.company;


import org.capstone.job_fair.models.dtos.company.JobFairBoothLayoutDTO;
import org.capstone.job_fair.models.entities.company.JobFairBoothLayoutEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {JobFairBoothMapper.class, CompanyBoothLayoutVideoMapper.class}
)
public abstract class CompanyBoothLayoutMapper {
    public abstract JobFairBoothLayoutEntity toEntity(JobFairBoothLayoutDTO dto);

    public abstract JobFairBoothLayoutDTO toDTO(JobFairBoothLayoutEntity entity);

}
