package org.capstone.job_fair.services.mappers.company;

import org.capstone.job_fair.models.dtos.company.CompanyRegistrationDTO;
import org.capstone.job_fair.models.entities.company.CompanyRegistrationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {SubCategoryMapper.class, BenefitEntityMapper.class, MediaEntityMapper.class, RegistrationJobPositionMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CompanyRegistrationMapper {

    public abstract CompanyRegistrationDTO toDTO(CompanyRegistrationEntity entity);

    public abstract CompanyRegistrationEntity toEntity(CompanyRegistrationDTO dto);
}
