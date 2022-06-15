package org.capstone.job_fair.services.mappers.company.layout;

import org.capstone.job_fair.models.dtos.company.layout.DecoratorBoothLayoutDTO;
import org.capstone.job_fair.models.entities.company.layout.DecoratorBoothLayoutEntity;
import org.capstone.job_fair.services.mappers.company.CompanyEmployeeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {CompanyEmployeeMapper.class, DecoratorBoothLayoutVideoMapper.class}
)
public abstract class DecoratorBoothLayoutMapper {
    public abstract DecoratorBoothLayoutDTO toDTO(DecoratorBoothLayoutEntity entity);

    public abstract DecoratorBoothLayoutEntity toEntity(DecoratorBoothLayoutDTO dto);
}
