package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.models.dtos.company.CompanyBenefitDTO;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.entities.company.CompanyBenefitEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BenefitEntityMapper.class})
public interface CompanyBenefitMapper   {
    @Mapping(source = "benefit", target = "benefitDTO")
    CompanyBenefitDTO toDTO(CompanyBenefitEntity entity);

    @Mapping(source = "benefitDTO", target = "benefit")
    CompanyBenefitEntity toEntity(CompanyBenefitDTO dto);
}
