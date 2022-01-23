package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.controllers.payload.requests.UpdateAttendantRequest;
import org.capstone.job_fair.models.dtos.attendant.cv.ReferenceDTO;
import org.capstone.job_fair.models.entities.attendant.cv.ReferenceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ReferenceMapper {
    public abstract ReferenceEntity toEntity(ReferenceDTO dto);
    public abstract ReferenceDTO toDTO(ReferenceEntity entity);
    public abstract ReferenceDTO toDTO(UpdateAttendantRequest.References request);
}
