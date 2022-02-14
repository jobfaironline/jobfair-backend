package org.capstone.job_fair.services.mappers.attendant;

import org.capstone.job_fair.controllers.payload.requests.attendant.UpdateAttendantRequest;
import org.capstone.job_fair.models.dtos.attendant.cv.CertificationDTO;
import org.capstone.job_fair.models.entities.attendant.cv.CertificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CertificationMapper {
    public abstract CertificationEntity toEntity(CertificationDTO dto);

    public abstract CertificationDTO toDTO(CertificationEntity entity);

    public abstract CertificationDTO toDTO(UpdateAttendantRequest.Certifications request);

    public abstract void updateCertificationEntityFromCertificationDTO(CertificationDTO dto, @MappingTarget CertificationEntity entity);
}
