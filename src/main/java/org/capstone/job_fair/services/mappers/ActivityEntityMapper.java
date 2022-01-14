package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.models.dtos.attendant.cv.ActivityDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.CertificationDTO;
import org.capstone.job_fair.models.entities.attendant.cv.ActivityEntity;
import org.capstone.job_fair.models.entities.attendant.cv.CertificationEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public abstract class ActivityEntityMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void DTOToEntity(ActivityDTO dto, @MappingTarget ActivityEntity entity);
}
