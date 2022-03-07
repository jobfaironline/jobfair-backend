package org.capstone.job_fair.services.mappers.job_fair;

import org.capstone.job_fair.models.dtos.job_fair.CompanyJobFairStatusDTO;
import org.capstone.job_fair.models.entities.job_fair.CompanyJobFairStatusEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {JobFairMapper.class}
)
public abstract class CompanyJobFairStatusMapper {
    public abstract CompanyJobFairStatusEntity toEntity(CompanyJobFairStatusDTO dto);

    public abstract CompanyJobFairStatusDTO toDTO(CompanyJobFairStatusEntity entity);
}
