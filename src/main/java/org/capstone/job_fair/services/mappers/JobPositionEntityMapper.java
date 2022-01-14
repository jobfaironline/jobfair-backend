package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.company.job.JobPositionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
public abstract class JobPositionEntityMapper {
    public abstract JobPositionDTO toDTO(JobPositionEntity jobPosition);
    public abstract JobPositionEntity toEntity(JobPositionDTO jobPositionDTO);
    public abstract void updateAccountMapperFromDto(AccountDTO dto, @MappingTarget AccountEntity entity);
}
