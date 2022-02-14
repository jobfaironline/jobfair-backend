package org.capstone.job_fair.services.mappers.company;

import org.capstone.job_fair.models.dtos.company.BenefitDTO;
import org.capstone.job_fair.models.entities.company.BenefitEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class BenefitEntityMapper {

    public abstract BenefitEntity toEntity(BenefitDTO dto);

    public abstract BenefitDTO toDTO(BenefitEntity entity);

}
