package org.capstone.job_fair.services.mappers.job_fair;


import org.capstone.job_fair.models.dtos.job_fair.ShiftDTO;
import org.capstone.job_fair.models.entities.job_fair.ShiftEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ShiftMapper {
    public abstract ShiftDTO toDTO(ShiftEntity entity);

    public abstract ShiftEntity toEntity(ShiftDTO dto);

    public abstract void updateFromDTO(@MappingTarget ShiftEntity entity, ShiftDTO dto);
}
