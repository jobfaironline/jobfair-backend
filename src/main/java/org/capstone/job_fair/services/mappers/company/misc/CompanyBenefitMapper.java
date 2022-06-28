package org.capstone.job_fair.services.mappers.company.misc;

import org.capstone.job_fair.models.dtos.company.misc.CompanyBenefitDTO;
import org.capstone.job_fair.models.entities.company.misc.CompanyBenefitEntity;
import org.capstone.job_fair.services.mappers.company.CompanyMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        uses = {BenefitEntityMapper.class, CompanyMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CompanyBenefitMapper {
    @Mapping(source = "benefit", target = "benefitDTO")
    @Mapping(target = "companyDTO", ignore = true)
    public abstract CompanyBenefitDTO toDTO(CompanyBenefitEntity entity);

    @Mapping(source = "benefitDTO", target = "benefit")
    @Mapping(target = "company", ignore = true)
    public abstract CompanyBenefitEntity toEntity(CompanyBenefitDTO dto);

    @Mapping(source = "benefitDTO", target = "benefit")
    @Mapping(source = "companyDTO", target = "company")
    public abstract void updateCompanyBenefitEntity(CompanyBenefitDTO dto, @MappingTarget CompanyBenefitEntity entity);
}
