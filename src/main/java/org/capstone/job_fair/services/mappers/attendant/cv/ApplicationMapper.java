package org.capstone.job_fair.services.mappers.attendant.cv;


import org.capstone.job_fair.controllers.payload.responses.ApplicationForAttendantResponse;
import org.capstone.job_fair.controllers.payload.responses.ApplicationForCompanyResponse;
import org.capstone.job_fair.controllers.payload.responses.ApplicationWithGenralDataOfApplicantResponse;
import org.capstone.job_fair.models.dtos.attendant.cv.ApplicationDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.attendant.cv.ApplicationEntity;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.services.mappers.account.AccountMapper;
import org.capstone.job_fair.services.mappers.attendant.AttendantMapper;
import org.capstone.job_fair.services.mappers.attendant.CountryMapper;
import org.capstone.job_fair.services.mappers.company.RegistrationJobPositionMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {AccountMapper.class, AttendantMapper.class, CvMapper.class, RegistrationJobPositionMapper.class, CountryMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ApplicationMapper {

    @Mapping(source = "boothJobPosition", target = "boothJobPositionDTO")
    @Mapping(source = "cv", target = "cvDTO")
    public abstract ApplicationDTO toDTO(ApplicationEntity entity);

    @Mapping(target = "boothJobPosition", source = "boothJobPositionDTO")
    @Mapping(target = "cv", source = "cvDTO")
    public abstract ApplicationEntity toEntity(ApplicationDTO dto);

    public abstract void updateFromDTO(@MappingTarget ApplicationEntity entity, ApplicationDTO dto);


    @Mapping(target = "candidateName", source = "cv.attendant.account", qualifiedByName = "toFullName")
    @Mapping(target = "appliedDate", source = "createDate")
    @Mapping(target = "jobPositionTitle", source = "boothJobPosition.title")
    @Mapping(target = "jobPositionId", source = "boothJobPosition.id")
    public abstract ApplicationForCompanyResponse toApplicationForCompanyResponse(ApplicationEntity entity);


    @Mapping(target = "appliedDate", source = "createDate")
    @Mapping(target = "candidateName", source = "cv.attendant.account", qualifiedByName = "toFullName")
    @Mapping(target = "applicationSummary", source = "summary")
    @Mapping(target = "jobPositionTitle", source = "boothJobPosition.title")
    @Mapping(target = "jobPositionId", source = "boothJobPosition.id")
    @Mapping(target = "candidateYearOfExp", source = "cv.yearOfExp")
    @Mapping(target = "candidateJobLevel", source = "cv.jobLevel")
    @Mapping(target = "candidateJobTitle", source = "cv.jobTitle")
    @Mapping(target = "candidateSkills", source = "cv.skills")
    @Mapping(target = "candidateActivities", source = "cv.activities")
    @Mapping(target = "candidateCertifications", source = "cv.certifications")
    @Mapping(target = "candidateEducation", source = "cv.educations")
    @Mapping(target = "candidateReferences", source = "cv.references")
    @Mapping(target = "candidateWorkHistories", source = "cv.workHistories")
    @Mapping(target = "status", source="status")
    @Mapping(target = "gender", source="cv.attendant.account" , qualifiedByName = "toGender")
    @Mapping(target = "imageUrl", source="cv.attendant.account.profileImageUrl")
    @Mapping(target = "country", source="cv.attendant.country.name")
    @Mapping(target = "dob", source="cv.attendant.dob")
    public abstract ApplicationWithGenralDataOfApplicantResponse toApplicationWithGenralDataOfApplicantResponse(ApplicationEntity entity);

    @Mapping(target = "authorizerName", source = "authorizer", qualifiedByName = "toFullName")
    @Mapping(target = "jobPositionId", source = "boothJobPosition.id")
    @Mapping(target = "jobPositionTitle", source = "boothJobPosition.title")
    public abstract ApplicationForAttendantResponse toApplicationForAttendantResponse(ApplicationEntity entity);

    @Named("toFullName")
    public String toFullName(AccountEntity accountEntity) {
        if (accountEntity == null) return null;
        return accountEntity.getFullname();

    }

    @Named("toGender")
    public Gender toGender(AccountEntity accountEntity) {
        if (accountEntity == null) return null;
        return Gender.values()[accountEntity.getGender().getId()];
    }


}
