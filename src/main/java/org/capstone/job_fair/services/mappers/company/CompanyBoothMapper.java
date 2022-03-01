package org.capstone.job_fair.services.mappers.company;

import org.capstone.job_fair.models.dtos.company.CompanyBoothDTO;
import org.capstone.job_fair.models.entities.company.CompanyBoothEntity;
import org.capstone.job_fair.services.mappers.job_fair.BoothMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {OrderMapper.class, BoothMapper.class}
)
public abstract class CompanyBoothMapper {
    public abstract CompanyBoothDTO toDTO(CompanyBoothEntity entity);

    public abstract CompanyBoothEntity toEntity(CompanyBoothDTO dto);
}
