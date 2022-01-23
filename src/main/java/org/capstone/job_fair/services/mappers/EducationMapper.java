package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.controllers.payload.requests.UpdateAttendantRequest;
import org.capstone.job_fair.models.dtos.attendant.cv.EducationDTO;
import org.capstone.job_fair.models.entities.attendant.cv.EducationEntity;
import org.capstone.job_fair.models.enums.Qualification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class EducationMapper {
    public abstract EducationEntity toEntity(EducationDTO dto);
    public abstract EducationDTO toDTO(EducationEntity entity);

    public abstract EducationDTO toDTO(UpdateAttendantRequest.Educations request);


}
