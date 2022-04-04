package org.capstone.job_fair.services.mappers.job_fair;

import org.capstone.job_fair.controllers.payload.responses.JobFairForCompanyResponse;
import org.capstone.job_fair.models.dtos.job_fair.CompanyJobFairStatusDTO;
import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {JobFairMapper.class}
)
public abstract class CompanyJobFairStatusMapper {
    public abstract CompanyJobFairStatusEntity toEntity(CompanyJobFairStatusDTO dto);

    public abstract CompanyJobFairStatusDTO toDTO(CompanyJobFairStatusEntity entity);

    @Mapping(target = "status", ignore = true)
    public abstract JobFairForCompanyResponse toJobFairForCompanyResponse(JobFairDTO dto);
}
