package org.capstone.job_fair.services.mappers.job_fair;

import org.capstone.job_fair.models.dtos.job_fair.InterviewScheduleDTO;
import org.capstone.job_fair.models.entities.attendant.application.ApplicationEntity;
import org.capstone.job_fair.services.mappers.attendant.AttendantMapper;
import org.capstone.job_fair.services.mappers.company.CompanyEmployeeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {
        CompanyEmployeeMapper.class, AttendantMapper.class
})
public abstract class InterviewScheduleMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "endTime", target = "endTime")
    @Mapping(source = "beginTime", target = "beginTime")
    @Mapping(source = "interviewName", target = "name")
    @Mapping(source = "interviewDescription", target = "description")
    @Mapping(source = "interviewStatus", target = "status")
    @Mapping(source = "interviewUrl", target = "url")
    @Mapping(source = "interviewer.accountId", target = "interviewerId")
    @Mapping(source = "attendant.accountId", target = "attendantId")
    @Mapping(source = "boothJobPosition.jobFairBooth.jobFair.publicEndTime", target = "jobFairPublicEndTime")
    public abstract InterviewScheduleDTO toDTO(ApplicationEntity entity);
}
