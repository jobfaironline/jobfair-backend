package org.capstone.job_fair.services.mappers.company;

import org.capstone.job_fair.models.dtos.company.JobFairBoothDTO;
import org.capstone.job_fair.models.entities.company.JobFairBoothEntity;
import org.capstone.job_fair.services.mappers.job_fair.BoothMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {OrderMapper.class, BoothMapper.class}
)
public abstract class CompanyBoothMapper {
    public abstract JobFairBoothDTO toDTO(JobFairBoothEntity entity);

    public abstract JobFairBoothEntity toEntity(JobFairBoothDTO dto);
}
