package org.capstone.job_fair.services.mappers.attendant.cv;


import org.capstone.job_fair.controllers.payload.responses.ApplicationForAttendantResponse;
import org.capstone.job_fair.controllers.payload.responses.ApplicationForCompanyResponse;
import org.capstone.job_fair.controllers.payload.responses.ApplicationWithGenralDataOfApplicantResponse;
import org.capstone.job_fair.models.dtos.attendant.cv.ApplicationDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.attendant.cv.ApplicationEntity;
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


    @Mapping(target = "candidateName", source = "cv.attendant.account", qualifiedByName = "toFullName")
    @Mapping(target = "appliedDate", source = "createDate")
    @Mapping(target = "jobPositionTitle", source = "registrationJobPosition.title")
    @Mapping(target = "jobFairName", source = "registrationJobPosition.companyRegistration.jobFairEntity.name")
    @Mapping(target = "jobFairId", source = "registrationJobPosition.companyRegistration.jobFairEntity.id")
    @Mapping(target = "jobPositionId", source = "registrationJobPosition.id")
    public abstract ApplicationForCompanyResponse toApplicationForCompanyResponse(ApplicationEntity entity);


    @Mapping(target = "appliedDate", source = "createDate")
    @Mapping(target = "candidateName", source = "cv.attendant.account", qualifiedByName = "toFullName")
    @Mapping(target = "applicationSummary", source = "summary")
    @Mapping(target = "jobPositionTitle", source = "registrationJobPosition.title")
    @Mapping(target = "jobFairName", source = "registrationJobPosition.companyRegistration.jobFairEntity.name")
    @Mapping(target = "jobFairId", source = "registrationJobPosition.companyRegistration.jobFairEntity.id")
    @Mapping(target = "jobPositionId", source = "registrationJobPosition.id")
    @Mapping(target = "candidateYearOfExp", source = "cv.yearOfExp")
    @Mapping(target = "candidateJobLevel", source = "cv.jobLevel")
    @Mapping(target = "candidateJobTitle", source = "cv.jobTitle")
    @Mapping(target = "candidateSkills", source = "cv.skills")
    public abstract ApplicationWithGenralDataOfApplicantResponse toApplicationWithGenralDataOfApplicantResponse(ApplicationEntity entity);

    @Mapping(target = "jobFairName", source = "registrationJobPosition.companyRegistration.jobFairEntity.name")
    @Mapping(target = "authorizerName", source = "authorizer", qualifiedByName = "toFullName")
    @Mapping(target = "jobFairId", source = "registrationJobPosition.companyRegistration.jobFairEntity.id")
    @Mapping(target = "jobPositionId", source = "registrationJobPosition.id")
    @Mapping(target = "jobPositionTitle", source = "registrationJobPosition.title")
    public abstract ApplicationForAttendantResponse toApplicationForAttendantResponse(ApplicationEntity entity);

    @Named("toFullName")
    public String toFullName(AccountEntity accountEntity) {
        if (accountEntity == null) return null;
        return accountEntity.getFullname();

    }


}
