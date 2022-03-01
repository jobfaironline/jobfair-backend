package org.capstone.job_fair.services.mappers.company;

import org.capstone.job_fair.models.dtos.company.OrderDTO;
import org.capstone.job_fair.models.entities.company.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {CompanyRegistrationMapper.class}
)
public abstract class OrderMapper {
    public abstract OrderEntity toEntity(OrderDTO dto);

    public abstract OrderDTO toDTO(OrderEntity entity);
}
