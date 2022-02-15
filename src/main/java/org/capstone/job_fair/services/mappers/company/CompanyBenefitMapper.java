package org.capstone.job_fair.services.mappers.company;

import org.capstone.job_fair.models.dtos.company.CompanyBenefitDTO;
import org.capstone.job_fair.models.entities.company.CompanyBenefitEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        uses = {BenefitEntityMapper.class, CompanyMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CompanyBenefitMapper {
    @Mapping(source = "benefit", target = "benefitDTO")
    @Mapping(source = "company", target = "companyDTO")
    public abstract CompanyBenefitDTO toDTO(CompanyBenefitEntity entity);

    @Mapping(source = "benefitDTO", target = "benefit")
    @Mapping(source = "companyDTO", target = "company")
    public abstract CompanyBenefitEntity toEntity(CompanyBenefitDTO dto);

    @Mapping(source = "benefitDTO", target = "benefit")
    @Mapping(source = "companyDTO", target = "company")
    public abstract void updateCompanyBenefitEntity(CompanyBenefitDTO dto, @MappingTarget CompanyBenefitEntity entity);
}
