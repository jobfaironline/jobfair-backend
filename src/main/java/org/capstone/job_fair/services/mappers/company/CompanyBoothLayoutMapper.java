package org.capstone.job_fair.services.mappers.company;


import org.capstone.job_fair.models.dtos.company.CompanyBoothLayoutDTO;
import org.capstone.job_fair.models.entities.company.CompanyBoothLayoutEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {CompanyBoothMapper.class, CompanyBoothLayoutVideoMapper.class}
)
public abstract class CompanyBoothLayoutMapper {
    public abstract CompanyBoothLayoutEntity toEntity(CompanyBoothLayoutDTO dto);

    public abstract CompanyBoothLayoutDTO toDTO(CompanyBoothLayoutEntity entity);

}
