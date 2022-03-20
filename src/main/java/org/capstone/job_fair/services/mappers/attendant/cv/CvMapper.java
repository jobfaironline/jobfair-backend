package org.capstone.job_fair.services.mappers.attendant.cv;

import org.capstone.job_fair.models.dtos.attendant.cv.CvDTO;
import org.capstone.job_fair.models.entities.attendant.cv.CvEntity;
import org.capstone.job_fair.services.mappers.attendant.AttendantMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        uses = {AttendantMapper.class, CvCertificationMapper.class, CvEducationMapper.class, CvReferenceMapper.class,
                CvSkillMapper.class, CvWorkHistoryMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CvMapper {
    public abstract CvDTO toDTO(CvEntity entity);

    public abstract CvEntity toEntity(CvDTO dto);
}
