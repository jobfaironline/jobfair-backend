package org.capstone.job_fair.services.mappers.company;

import org.capstone.job_fair.models.dtos.company.CompanyBoothDTO;
import org.capstone.job_fair.models.entities.company.CompanyBoothEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CompanyBoothMapper {
    public abstract CompanyBoothDTO toDTO(CompanyBoothEntity entity);
    public abstract CompanyBoothEntity toEntity(CompanyBoothDTO dto);
}
