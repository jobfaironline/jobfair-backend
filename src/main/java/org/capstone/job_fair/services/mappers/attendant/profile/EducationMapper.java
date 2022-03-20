package org.capstone.job_fair.services.mappers.attendant.profile;

import org.capstone.job_fair.controllers.payload.requests.attendant.UpdateAttendantRequest;
import org.capstone.job_fair.models.dtos.attendant.profile.EducationDTO;
import org.capstone.job_fair.models.entities.attendant.profile.EducationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class EducationMapper {
    public abstract EducationEntity toEntity(EducationDTO dto);

    public abstract EducationDTO toDTO(EducationEntity entity);

    public abstract EducationDTO toDTO(UpdateAttendantRequest.Educations request);

    public abstract void updateEducationEntityFromEducationDTO(EducationDTO dto, @MappingTarget EducationEntity entity);

}
