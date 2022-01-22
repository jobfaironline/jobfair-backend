package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.controllers.payload.requests.UpdateAttendantRequest;
import org.capstone.job_fair.models.dtos.attendant.cv.CertificationDTO;
import org.capstone.job_fair.models.entities.attendant.cv.CertificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CertificationEntityMapper {
    public abstract CertificationEntity toEntity(CertificationDTO dto);

    public abstract CertificationDTO toDTO(UpdateAttendantRequest.CertificateRequest request);
}
