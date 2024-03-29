package org.capstone.job_fair.services.mappers.company.misc;

import org.capstone.job_fair.models.dtos.company.misc.BenefitDTO;
import org.capstone.job_fair.models.entities.company.misc.BenefitEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class BenefitEntityMapper {

    public abstract BenefitEntity toEntity(BenefitDTO dto);

    public abstract BenefitDTO toDTO(BenefitEntity entity);

}
