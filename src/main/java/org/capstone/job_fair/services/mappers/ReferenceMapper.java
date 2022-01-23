package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.controllers.payload.requests.UpdateAttendantRequest;
import org.capstone.job_fair.models.dtos.attendant.cv.ReferenceDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.SkillDTO;
import org.capstone.job_fair.models.entities.attendant.cv.ReferenceEntity;
import org.capstone.job_fair.models.entities.attendant.cv.SkillEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ReferenceMapper {
    public abstract ReferenceEntity toEntity(ReferenceDTO dto);
    public abstract ReferenceDTO toDTO(ReferenceEntity entity);
    public abstract ReferenceDTO toDTO(UpdateAttendantRequest.References request);

    public abstract void updateReferenceEntityFromRefereceDTO(ReferenceDTO dto, @MappingTarget ReferenceEntity entity);
}
