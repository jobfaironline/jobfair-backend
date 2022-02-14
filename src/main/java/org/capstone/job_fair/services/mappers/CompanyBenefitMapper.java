package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.models.dtos.company.CompanyBenefitDTO;
import org.capstone.job_fair.models.entities.company.CompanyBenefitEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {BenefitEntityMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CompanyBenefitMapper {
    @Mapping(source = "benefit", target = "benefitDTO")
    public abstract CompanyBenefitDTO toDTO(CompanyBenefitEntity entity);

    @Mapping(source = "benefitDTO", target = "benefit")
    public abstract CompanyBenefitEntity toEntity(CompanyBenefitDTO dto);

    public abstract void updateCompanyBenefitEntity(CompanyBenefitDTO dto, @MappingTarget CompanyBenefitEntity entity);
}
