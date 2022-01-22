package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {CompanyEntityMapper.class, AccountEntityMapper
        .class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CompanyEmployeeEntityMapper {
    @Mapping(source = "company", target = "companyDTO")
    public abstract CompanyEmployeeDTO toDTO(CompanyEmployeeEntity attendant);

    @Mapping(source = "companyDTO", target = "company")
    public abstract CompanyEmployeeEntity toEntity(CompanyEmployeeDTO dto);

    public abstract void updateCompanyEmployeeMapperFromDto(CompanyEmployeeDTO dto, @MappingTarget CompanyEmployeeEntity entity);
}
