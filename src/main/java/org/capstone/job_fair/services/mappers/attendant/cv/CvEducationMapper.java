package org.capstone.job_fair.services.mappers.attendant.cv;

import org.capstone.job_fair.models.dtos.attendant.cv.CvEducationDTO;
import org.capstone.job_fair.models.entities.attendant.cv.CvEducationEntity;
import org.capstone.job_fair.services.mappers.attendant.AttendantMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {AttendantMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CvEducationMapper {

    public abstract CvEducationDTO toDTO(CvEducationEntity entity);

    public abstract CvEducationEntity toEntity(CvEducationDTO dto);
}
