package org.capstone.job_fair.services.mappers.attendant.cv;


import org.capstone.job_fair.controllers.payload.responses.ApplicationForCompanyResponse;
import org.capstone.job_fair.models.dtos.attendant.cv.ApplicationDTO;
import org.capstone.job_fair.models.entities.attendant.cv.ApplicationEntity;
import org.capstone.job_fair.models.entities.attendant.cv.CvEntity;
import org.capstone.job_fair.services.mappers.account.AccountMapper;
import org.capstone.job_fair.services.mappers.attendant.AttendantMapper;
import org.capstone.job_fair.services.mappers.company.RegistrationJobPositionMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {AccountMapper.class, AttendantMapper.class, CvMapper.class, RegistrationJobPositionMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ApplicationMapper {

    @Mapping(source = "registrationJobPosition", target = "registrationJobPositionDTO")
    @Mapping(source = "cv", target = "cvDTO")
    public abstract ApplicationDTO toDTO(ApplicationEntity entity);

    @Mapping(target = "registrationJobPosition", source = "registrationJobPositionDTO")
    @Mapping(target = "cv", source = "cvDTO")
    public abstract ApplicationEntity toEntity(ApplicationDTO dto);

    public abstract void updateFromDTO(@MappingTarget ApplicationEntity entity, ApplicationDTO dto);


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
