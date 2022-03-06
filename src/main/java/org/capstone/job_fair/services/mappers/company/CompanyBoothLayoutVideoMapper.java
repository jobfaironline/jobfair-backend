package org.capstone.job_fair.services.mappers.company;

import org.capstone.job_fair.models.dtos.company.CompanyBoothLayoutVideoDTO;
import org.capstone.job_fair.models.entities.company.CompanyBoothLayoutVideoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class CompanyBoothLayoutVideoMapper {
    public abstract CompanyBoothLayoutVideoEntity toEntity(CompanyBoothLayoutVideoDTO dto);

    public abstract CompanyBoothLayoutVideoDTO toDTO(CompanyBoothLayoutVideoEntity entity);
}
