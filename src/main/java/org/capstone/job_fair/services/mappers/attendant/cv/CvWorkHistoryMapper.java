package org.capstone.job_fair.services.mappers.attendant.cv;

import org.capstone.job_fair.controllers.payload.requests.attendant.cv.DraftCvRequest;
import org.capstone.job_fair.models.dtos.attendant.cv.CvWorkHistoryDTO;
import org.capstone.job_fair.models.entities.attendant.cv.CvWorkHistoryEntity;
import org.capstone.job_fair.services.mappers.attendant.AttendantMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {AttendantMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CvWorkHistoryMapper {
    public abstract CvWorkHistoryDTO toDTO(CvWorkHistoryEntity entity);

    @Mapping(target = "cv", ignore = true)
    public abstract CvWorkHistoryEntity toEntity(CvWorkHistoryDTO dto);

    @Mapping(target = "id", ignore = true)
    public abstract CvWorkHistoryDTO toDTO(DraftCvRequest.WorkHistories request);

    public abstract void updateCvWorkHistoryEntityFromCvWorkHistoryDTO(CvWorkHistoryDTO dto, @MappingTarget CvWorkHistoryEntity entity);

}
