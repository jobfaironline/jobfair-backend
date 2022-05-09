package org.capstone.job_fair.services.mappers.job_fair.booth;


import org.capstone.job_fair.models.dtos.job_fair.booth.JobFairBoothLayoutDTO;
import org.capstone.job_fair.models.entities.job_fair.booth.JobFairBoothLayoutEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {JobFairBoothMapper.class, CompanyBoothLayoutVideoMapper.class}
)
public abstract class JobFairBoothLayoutMapper {
    public abstract JobFairBoothLayoutEntity toEntity(JobFairBoothLayoutDTO dto);

    public abstract JobFairBoothLayoutDTO toDTO(JobFairBoothLayoutEntity entity);

}
