package org.capstone.job_fair.services.mappers;


import org.capstone.job_fair.models.dtos.attendant.cv.WorkHistoryDTO;
import org.capstone.job_fair.models.entities.attendant.cv.WorkHistoryEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public abstract class WorkHistoryEntityMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void DTOToEntity(WorkHistoryDTO dto, @MappingTarget WorkHistoryEntity entity);
}
