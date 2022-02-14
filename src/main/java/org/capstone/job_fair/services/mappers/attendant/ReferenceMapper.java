package org.capstone.job_fair.services.mappers.attendant;

import org.capstone.job_fair.controllers.payload.requests.UpdateAttendantRequest;
import org.capstone.job_fair.models.dtos.attendant.cv.ReferenceDTO;
import org.capstone.job_fair.models.entities.attendant.cv.ReferenceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ReferenceMapper {
    @Mapping(source = "fullname", target = "fullName")
    @Mapping(source = "phone", target = "phoneNumber")
    public abstract ReferenceEntity toEntity(ReferenceDTO dto);

    @Mapping(source = "fullName", target = "fullname")
    @Mapping(source = "phoneNumber", target = "phone")
    public abstract ReferenceDTO toDTO(ReferenceEntity entity);

    public abstract ReferenceDTO toDTO(UpdateAttendantRequest.References request);

    @Mapping(source = "fullname", target = "fullName")
    @Mapping(source = "phone", target = "phoneNumber")
    public abstract void updateReferenceEntityFromRefereceDTO(ReferenceDTO dto, @MappingTarget ReferenceEntity entity);
}
