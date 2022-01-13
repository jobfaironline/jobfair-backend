package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AccountEntityMapper.class})
public abstract class CompanyEmployeeEntityMapper {
    public abstract CompanyEmployeeDTO toDTO(CompanyEmployeeEntity attendant);
    public abstract CompanyEmployeeEntity toEntity(CompanyEmployeeDTO dto);
}
