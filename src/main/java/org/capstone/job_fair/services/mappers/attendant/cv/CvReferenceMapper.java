package org.capstone.job_fair.services.mappers.attendant.cv;

import org.capstone.job_fair.models.dtos.attendant.cv.CvReferenceDTO;
import org.capstone.job_fair.models.entities.attendant.cv.CvReferenceEntity;
import org.capstone.job_fair.services.mappers.attendant.AttendantMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {AttendantMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CvReferenceMapper {

    public abstract CvReferenceDTO toDTO(CvReferenceEntity entity);

    public abstract CvReferenceEntity toEntity(CvReferenceDTO dto);
}
