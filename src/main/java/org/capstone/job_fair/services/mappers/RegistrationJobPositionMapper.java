package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.models.dtos.company.job.RegistrationJobPositionDTO;
import org.capstone.job_fair.models.entities.company.job.RegistrationJobPositionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {SubCategoryMapper.class})
public abstract class RegistrationJobPositionMapper {

    public abstract RegistrationJobPositionEntity toEntity(RegistrationJobPositionDTO jobPositionDTO);

}
