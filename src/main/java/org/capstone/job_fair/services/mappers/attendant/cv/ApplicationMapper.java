package org.capstone.job_fair.services.mappers.attendant.cv;


import org.capstone.job_fair.controllers.payload.responses.ApplicationForCompanyResponse;
import org.capstone.job_fair.models.dtos.attendant.cv.ApplicationDTO;
import org.capstone.job_fair.models.entities.attendant.cv.ApplicationEntity;
import org.capstone.job_fair.models.entities.attendant.cv.CvEntity;
import org.capstone.job_fair.services.mappers.attendant.AttendantMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {AttendantMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ApplicationMapper {

    public abstract ApplicationDTO toDTO(ApplicationEntity entity);

    @Mapping(target = "candidateName", source = "cv", qualifiedByName = "toCandidateName")
    @Mapping(target = "appliedDate", source = "createDate")
    @Mapping(target = "jobPositionTitle", source = "registrationJobPosition.title")
    @Mapping(target = "jobFairName", source = "registrationJobPosition.companyRegistration.jobFairEntity.name")
    @Mapping(target = "jobFairId", source = "registrationJobPosition.companyRegistration.jobFairEntity.id")
    @Mapping(target = "jobPositionId", source = "registrationJobPosition.id")
    public abstract ApplicationForCompanyResponse toApplicationForCompanyResponse(ApplicationEntity entity);

    @Named("toCandidateName")
    public String toCandidateName(CvEntity cv) {
        return cv.getAttendant().getAccount().getFullname();
    }
}
