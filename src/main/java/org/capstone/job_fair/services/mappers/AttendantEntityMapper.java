package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {AccountEntityMapper.class})
public abstract class AttendantEntityMapper {
    public abstract AttendantDTO toDTO(AttendantEntity attendant);
    public abstract AttendantEntity toEntity(AttendantDTO dto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    public abstract void updateAttendantMapperFromDto(AttendantDTO dto, @MappingTarget AttendantEntity entity);



}
