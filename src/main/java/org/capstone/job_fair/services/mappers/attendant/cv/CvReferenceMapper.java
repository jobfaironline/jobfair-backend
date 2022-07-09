package org.capstone.job_fair.services.mappers.attendant.cv;

import org.capstone.job_fair.controllers.payload.requests.attendant.cv.DraftCvRequest;
import org.capstone.job_fair.models.dtos.attendant.cv.CvReferenceDTO;
import org.capstone.job_fair.models.entities.attendant.cv.CvReferenceEntity;
import org.capstone.job_fair.services.mappers.attendant.AttendantMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {AttendantMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CvReferenceMapper {
    public abstract CvReferenceDTO toDTO(CvReferenceEntity entity);

    @Mapping(target = "cv", ignore = true)
    public abstract CvReferenceEntity toEntity(CvReferenceDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fullName", source = "fullname")
    @Mapping(target = "phoneNumber", source = "phone")
    public abstract CvReferenceDTO toDTO(DraftCvRequest.References request);

    public abstract void updateCvReferenceEntityFromCvReferenceDTO(CvReferenceDTO dto, @MappingTarget CvReferenceEntity entity);

}
