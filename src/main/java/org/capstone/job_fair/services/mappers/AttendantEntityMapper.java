package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {AccountEntityMapper.class})

public abstract class AttendantEntityMapper {
    public abstract AttendantDTO toDTO(AttendantEntity attendant);
    public abstract AttendantEntity toEntity(AttendantDTO dto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateAttendantMapperFromDto(AttendantDTO dto, @MappingTarget AttendantEntity entity);
}
