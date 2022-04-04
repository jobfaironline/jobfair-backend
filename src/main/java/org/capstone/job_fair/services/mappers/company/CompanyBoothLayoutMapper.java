package org.capstone.job_fair.services.mappers.company;


import org.capstone.job_fair.models.dtos.company.CompanyBoothLayoutDTO;
import org.capstone.job_fair.models.entities.company.JobFairBoothLayoutEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {CompanyBoothMapper.class, CompanyBoothLayoutVideoMapper.class}
)
public abstract class CompanyBoothLayoutMapper {
    public abstract JobFairBoothLayoutEntity toEntity(CompanyBoothLayoutDTO dto);

    public abstract CompanyBoothLayoutDTO toDTO(JobFairBoothLayoutEntity entity);

}
