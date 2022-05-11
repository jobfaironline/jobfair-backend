package org.capstone.job_fair.services.mappers.job_fair;

import org.capstone.job_fair.models.dtos.job_fair.InterviewRequestChangeDTO;
import org.capstone.job_fair.models.entities.job_fair.InterviewRequestChangeEntity;
import org.capstone.job_fair.services.mappers.account.AccountMapper;
import org.capstone.job_fair.services.mappers.attendant.application.ApplicationMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {
        AccountMapper.class, ApplicationMapper.class
})
public abstract class InterviewRequestChangeMapper {
    public abstract InterviewRequestChangeDTO toDTO(InterviewRequestChangeEntity entity);

    public abstract InterviewRequestChangeEntity toEntity(InterviewRequestChangeDTO dto);
}
