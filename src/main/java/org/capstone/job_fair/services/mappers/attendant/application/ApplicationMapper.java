package org.capstone.job_fair.services.mappers.attendant.application;


import org.capstone.job_fair.controllers.payload.responses.ApplicationForAttendantResponse;
import org.capstone.job_fair.controllers.payload.responses.ApplicationForCompanyResponse;
import org.capstone.job_fair.controllers.payload.responses.ApplicationWithGenralDataOfApplicantResponse;
import org.capstone.job_fair.models.dtos.attendant.application.ApplicationDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.attendant.application.ApplicationEntity;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.services.mappers.account.AccountMapper;
import org.capstone.job_fair.services.mappers.attendant.AttendantMapper;
import org.capstone.job_fair.services.mappers.attendant.cv.CvMapper;
import org.capstone.job_fair.services.mappers.attendant.misc.CountryMapper;
import org.capstone.job_fair.services.mappers.company.CompanyEmployeeMapper;
import org.capstone.job_fair.services.mappers.job_fair.booth.BoothJobPositionMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {
        AccountMapper.class,
        AttendantMapper.class,
        CvMapper.class,
        BoothJobPositionMapper.class,
        CountryMapper.class,
        ApplicationActivityMapper.class,
        ApplicationCertificationMapper.class,
        ApplicationEducationMapper.class,
        ApplicationReferenceMapper.class,
        ApplicationSkillMapper.class,
        ApplicationWorkHistoryMapper.class,
        CompanyEmployeeMapper.class,
}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ApplicationMapper {

    @Mapping(source = "boothJobPosition", target = "boothJobPositionDTO")
    public abstract ApplicationDTO toDTO(ApplicationEntity entity);

    @Mapping(target = "boothJobPosition", source = "boothJobPositionDTO")
    public abstract ApplicationEntity toEntity(ApplicationDTO dto);

    @Mapping(target = "boothJobPosition", source = "boothJobPositionDTO")
    public abstract void updateFromDTO(@MappingTarget ApplicationEntity entity, ApplicationDTO dto);


    @Mapping(target = "candidateName", source = "attendant.account", qualifiedByName = "toFullName")
    @Mapping(target = "appliedDate", source = "createDate")
    @Mapping(target = "jobPositionTitle", source = "boothJobPosition.title")
    @Mapping(target = "jobPositionId", source = "boothJobPosition.id")
    @Mapping(target = "jobFairName", source = "boothJobPosition.jobFairBooth.jobFair.name")
    @Mapping(target = "jobFairId", source = "boothJobPosition.jobFairBooth.jobFair.id")
    public abstract ApplicationForCompanyResponse toApplicationForCompanyResponse(ApplicationEntity entity);


    @Mapping(target = "appliedDate", source = "createDate")
    @Mapping(target = "candidateName", source = "attendant.account", qualifiedByName = "toFullName")
    @Mapping(target = "applicationSummary", source = "summary")
    @Mapping(target = "jobPositionTitle", source = "boothJobPosition.title")
    @Mapping(target = "jobPositionId", source = "boothJobPosition.id")
    @Mapping(target = "candidateYearOfExp", source = "yearOfExp")
    @Mapping(target = "candidateJobLevel", source = "jobLevel")
    @Mapping(target = "candidateJobTitle", source = "jobTitle")
    @Mapping(target = "candidateSkills", source = "skills")
    @Mapping(target = "candidateActivities", source = "activities")
    @Mapping(target = "candidateCertifications", source = "certifications")
    @Mapping(target = "candidateEducation", source = "educations")
    @Mapping(target = "candidateReferences", source = "references")
    @Mapping(target = "candidateWorkHistories", source = "workHistories")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "gender", source = "attendant.account", qualifiedByName = "toGender")
    @Mapping(target = "imageUrl", source = "attendant.account.profileImageUrl")
    @Mapping(target = "country", source = "attendant.country.name")
    @Mapping(target = "dob", source = "attendant.dob")
    @Mapping(target = "jobFairName", source = "boothJobPosition.jobFairBooth.jobFair.name")
    @Mapping(target = "jobFairId", source = "boothJobPosition.jobFairBooth.jobFair.id")
    @Mapping(target = "candidateFullName", source="fullName")
    @Mapping(target = "candidateProfileImageUrl", source="profileImageUrl")
    @Mapping(target = "candidateAboutMe", source="aboutMe")
    public abstract ApplicationWithGenralDataOfApplicantResponse toApplicationWithGenralDataOfApplicantResponse(ApplicationEntity entity);

    @Mapping(target = "authorizerName", source = "interviewer", qualifiedByName = "toEmployeeFullName")
    @Mapping(target = "jobPositionId", source = "boothJobPosition.id")
    @Mapping(target = "jobPositionTitle", source = "boothJobPosition.title")
    @Mapping(target = "jobFairName", source = "boothJobPosition.jobFairBooth.jobFair.name")
    @Mapping(target = "jobFairId", source = "boothJobPosition.jobFairBooth.jobFair.id")
    @Mapping(target = "boothName", source = "boothJobPosition.jobFairBooth.name")
    @Mapping(target = "companyName", source = "boothJobPosition.jobFairBooth.jobFair.company.name")
    public abstract ApplicationForAttendantResponse toApplicationForAttendantResponse(ApplicationEntity entity);

    @Named("toEmployeeFullName")
    public String toEmployeeFullName(CompanyEmployeeEntity employeeEntity) {
        if (employeeEntity == null) return null;
        return employeeEntity.getAccount().getFullname();
    }

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
