package org.capstone.job_fair.services.mappers.attendant.cv;


import org.capstone.job_fair.controllers.payload.responses.ApplicationForCompanyResponse;
import org.capstone.job_fair.controllers.payload.responses.ApplicationWithGenralDataOfApplicantResponse;
import org.capstone.job_fair.models.dtos.attendant.cv.ApplicationDTO;
import org.capstone.job_fair.models.entities.attendant.cv.ApplicationEntity;
import org.capstone.job_fair.models.entities.attendant.cv.CvEntity;
import org.capstone.job_fair.services.mappers.attendant.AttendantMapper;
import org.capstone.job_fair.services.mappers.company.RegistrationJobPositionMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {AttendantMapper.class, CvMapper.class, RegistrationJobPositionMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ApplicationMapper {

    @Mapping(source = "registrationJobPosition", target = "registrationJobPositionDTO")
    @Mapping(source = "cv", target = "cvDTO")
    public abstract ApplicationDTO toDTO(ApplicationEntity entity);

    @Mapping(target = "registrationJobPosition", source = "registrationJobPositionDTO")
    @Mapping(target = "cv", source = "cvDTO")
    public abstract ApplicationEntity toEntity(ApplicationDTO dto);


    @Mapping(target = "candidateName", source = "cv", qualifiedByName = "toCandidateName")
    @Mapping(target = "appliedDate", source = "createDate")
    @Mapping(target = "jobPositionTitle", source = "registrationJobPosition.title")
    @Mapping(target = "jobFairName", source = "registrationJobPosition.companyRegistration.jobFairEntity.name")
    @Mapping(target = "jobFairId", source = "registrationJobPosition.companyRegistration.jobFairEntity.id")
    @Mapping(target = "jobPositionId", source = "registrationJobPosition.id")
    public abstract ApplicationForCompanyResponse toApplicationForCompanyResponse(ApplicationEntity entity);


    @Mapping(target = "appliedDate", source = "createDate")
    @Mapping(target = "candidateName", source = "cv", qualifiedByName = "toCandidateName")
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

    @Named("toCandidateName")
    public String toCandidateName(CvEntity cv) {
        return cv.getAttendant().getAccount().getFullname();
    }
}
