package org.capstone.job_fair.services.mappers.attendant.cv;

import org.capstone.job_fair.controllers.payload.requests.attendant.cv.DraftCvRequest;
import org.capstone.job_fair.models.dtos.attendant.cv.CvCertificationDTO;
import org.capstone.job_fair.models.entities.attendant.cv.CvCertificationEntity;
import org.capstone.job_fair.services.mappers.attendant.AttendantMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {AttendantMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CvCertificationMapper {
    public abstract CvCertificationDTO toDTO(CvCertificationEntity entity);

    @Mapping(target = "cv", ignore = true)
    public abstract CvCertificationEntity toEntity(CvCertificationDTO dto);

    @Mapping(target = "id", ignore = true)
    public abstract CvCertificationDTO toDTO(DraftCvRequest.Certifications request);

    public abstract void updateCvCertificationEntityFromCvCertificationDTO(CvCertificationDTO dto, @MappingTarget CvCertificationEntity entity);

}
