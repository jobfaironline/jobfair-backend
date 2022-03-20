package org.capstone.job_fair.services.mappers.attendant.cv;

import org.capstone.job_fair.models.dtos.attendant.cv.CvCertificationDTO;
import org.capstone.job_fair.models.entities.attendant.cv.CvCertificationEntity;
import org.capstone.job_fair.services.mappers.attendant.AttendantMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {AttendantMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CvCertificationMapper {

    public abstract CvCertificationDTO toDTO(CvCertificationEntity entity);

    public abstract CvCertificationEntity toEntity(CvCertificationDTO dto);
}
