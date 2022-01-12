package org.capstone.job_fair.services.mappers;


import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public abstract class CompanyEntityMapper {
    public abstract CompanyDTO toDTO(CompanyEntity company);
    public abstract CompanyEntity toEntity(CompanyDTO dto);
}
