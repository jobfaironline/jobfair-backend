package org.capstone.job_fair.services.mappers.attendant.cv;

import org.capstone.job_fair.models.dtos.attendant.cv.CvWorkHistoryDTO;
import org.capstone.job_fair.models.entities.attendant.cv.CvWorkHistoryEntity;
import org.capstone.job_fair.services.mappers.attendant.AttendantMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {AttendantMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CvWorkHistoryMapper {
    public abstract CvWorkHistoryDTO toDTO(CvWorkHistoryEntity entity);

    public abstract CvWorkHistoryEntity toEntity(CvWorkHistoryDTO dto);
}
