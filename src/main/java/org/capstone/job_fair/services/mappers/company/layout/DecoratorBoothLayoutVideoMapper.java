package org.capstone.job_fair.services.mappers.company.layout;

import org.capstone.job_fair.models.dtos.company.layout.DecoratorBoothLayoutVideoDTO;
import org.capstone.job_fair.models.entities.company.layout.DecoratorBoothLayoutVideoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class DecoratorBoothLayoutVideoMapper {
    public abstract DecoratorBoothLayoutVideoDTO toDTO(DecoratorBoothLayoutVideoEntity entity);

    public abstract DecoratorBoothLayoutVideoEntity toEntity(DecoratorBoothLayoutVideoDTO dto);
}
