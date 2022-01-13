package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {AccountEntityMapper.class},  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
public abstract class CompanyEmployeeEntityMapper {
    public abstract CompanyEmployeeDTO toDTO(CompanyEmployeeEntity attendant);

    public abstract CompanyEmployeeEntity toEntity(CompanyEmployeeDTO dto);

    public abstract void updateCompanyEmployeeMapperFromDto(CompanyEmployeeDTO dto, @MappingTarget CompanyEmployeeEntity entity);
}
