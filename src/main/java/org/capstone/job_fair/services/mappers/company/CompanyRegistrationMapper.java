package org.capstone.job_fair.services.mappers.company;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {SubCategoryMapper.class, BenefitEntityMapper.class, MediaEntityMapper.class, RegistrationJobPositionMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CompanyRegistrationMapper {

    public abstract CompanyRegistrationDTO toDTO(CompanyRegistrationEntity entity);

    public abstract CompanyRegistrationEntity toEntity(CompanyRegistrationDTO dto);

    @Mapping(target = "createDate", ignore = true)
    public abstract void updateCompanyRegistrationEntityFromDTO(@MappingTarget CompanyRegistrationEntity entity, CompanyRegistrationDTO dto);

    public abstract CompanyRegistrationAdminDTO companyRegistrationAdminDTO(CompanyRegistrationAdminEntity entity);

}
