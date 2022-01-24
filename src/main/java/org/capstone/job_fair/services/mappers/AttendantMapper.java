package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.RegisterAttendantRequest;
import org.capstone.job_fair.controllers.payload.requests.UpdateAttendantRequest;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.*;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.capstone.job_fair.models.entities.attendant.CountryEntity;
import org.capstone.job_fair.models.entities.attendant.ResidenceEntity;
import org.capstone.job_fair.models.entities.attendant.cv.*;
import org.capstone.job_fair.utils.MessageUtil;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.NoSuchElementException;

@Mapper(componentModel = "spring", uses = {AccountMapper.class, SkillMapper.class, WorkHistoryMapper.class, EducationMapper.class, CertificationMapper.class, ReferenceMapper.class, ActivityMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED
)
public abstract class AttendantMapper {
    @Autowired
    private SkillMapper skillMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private WorkHistoryMapper workHistoryMapper;

    @Autowired
    private EducationMapper educationMapper;

    @Autowired
    private ReferenceMapper referenceMapper;

    @Autowired
    private CertificationMapper certificationMapper;


    @Mapping(target = "countryId", source = "country", qualifiedByName = "fromCountryEntityOfAttendantEntity")
    @Mapping(target = "residenceId", source = "residence", qualifiedByName = "fromResidenceEntityOfAttendantEntity")
    @Mapping(target = "jobLevel", source = "currentJobLevel")
    @Mapping(target = "skills", source = "skillEntities")
    @Mapping(target = "workHistories", source = "workHistoryEntities")
    @Mapping(target = "educations", source = "educationEntities")
    @Mapping(target = "certifications", source = "certificationEntities")
    @Mapping(target = "references", source = "referenceEntities")
    @Mapping(target = "activities", source = "activityEntities")
    public abstract AttendantDTO toDTO(AttendantEntity attendant);

    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "country", source = "countryId", qualifiedByName = "fromCountryIdOfAttendantDTO")
    @Mapping(target = "residence", source = "residenceId", qualifiedByName = "fromResidenceIdOfAttendantDTO")
    @Mapping(target = "currentJobLevel", source = "jobLevel")
    @Mapping(target = "skillEntities", source = "skills")
    @Mapping(target = "workHistoryEntities", source = "workHistories")
    @Mapping(target = "educationEntities", source = "educations")
    @Mapping(target = "certificationEntities", source = "certifications")
    @Mapping(target = "referenceEntities", source = "references")
    @Mapping(target = "activityEntities", source = "activities")
    public abstract AttendantEntity toEntity(AttendantDTO dto);

    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "country", source = "countryId", qualifiedByName = "fromCountryIdOfAttendantDTO")
    @Mapping(target = "residence", source = "residenceId", qualifiedByName = "fromResidenceIdOfAttendantDTO")
    @Mapping(target = "currentJobLevel", source = "jobLevel")
    @Mapping(target = "skillEntities", source = "skills")
    @Mapping(target = "workHistoryEntities", source = "workHistories")
    @Mapping(target = "educationEntities", source = "educations")
    @Mapping(target = "certificationEntities", source = "certifications")
    @Mapping(target = "referenceEntities", source = "references")
    @Mapping(target = "activityEntities", source = "activities")
    public abstract void updateAttendantMapperFromDto(AttendantDTO dto, @MappingTarget AttendantEntity entity);


    @Mapping(source = "accountId", target = "account.id")
    @Mapping(source = "account", target = "account")
    public abstract AttendantDTO toDTO(UpdateAttendantRequest request);


    @Mapping(source = "password", target = "account.password")
    @Mapping(source = "email", target = "account.email")
    @Mapping(source = "phone", target = "account.phone")
    @Mapping(source = "firstname", target = "account.firstname")
    @Mapping(source = "lastname", target = "account.lastname")
    @Mapping(source = "middlename", target = "account.middlename")
    @Mapping(source = "gender", target = "account.gender")
    public abstract AttendantDTO toDTO(RegisterAttendantRequest request);

    @Named("fromCountryIdOfAttendantDTO")
    public CountryEntity fromCountryIdOfAttendantDTO(String countryId) {
        if (countryId == null) return null;
        CountryEntity entity = new CountryEntity();
        entity.setId(countryId);
        return entity;
    }

    @Named("fromCountryEntityOfAttendantEntity")
    public String fromCountryEntityOfAttendantEntity(CountryEntity entity){
        return entity.getId();
    }

    @Named("fromResidenceIdOfAttendantDTO")
    public ResidenceEntity fromResidenceIdOfAttendantDTO(String residenceId) {
        if (residenceId == null) return null;
        ResidenceEntity entity = new ResidenceEntity();
        entity.setId(residenceId);
        return entity;
    }

    @Named("fromResidenceEntityOfAttendantEntity")
    public String fromResidenceEntityOfAttendantEntity(ResidenceEntity entity){
        return entity.getId();
    }

    public void mapToSkillEntities(List<SkillDTO> dtos, @MappingTarget List<SkillEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(skillDTO -> {
            if (skillDTO.getId() == null) { // if skill id null: create new skill
                if (!isValidSkillDTO(skillDTO)){
                    throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Skill.INVALID_SKILL));
                }
                SkillEntity entity = skillMapper.toEntity(skillDTO);
                entities.add(entity);
            } else {
                SkillEntity existedEntity = entities.stream().filter(skillEntity -> skillEntity.getId().equals(skillDTO.getId()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException(MessageUtil.getMessage(MessageConstant.Skill.SKILL_NOT_FOUND)));
                skillMapper.updateSkillEntityFromSkillDTO(skillDTO, existedEntity);
            }
        });
    }

    public void mapToWorkHistoryEntities(List<WorkHistoryDTO> dtos, @MappingTarget List<WorkHistoryEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(workHistoryDTO -> {
            if (workHistoryDTO.getId() == null) { // if skill id null: create new skill
                if (!isValidWorkHistoryDTO(workHistoryDTO)){
                    throw new IllegalArgumentException(MessageUtil.getMessage((MessageConstant.WorkHistory.INVALID_WORK_HISTORY)));
                }
                WorkHistoryEntity entity = workHistoryMapper.toEntity(workHistoryDTO);
                entities.add(entity);
            } else {
                WorkHistoryEntity existedEntity = entities.stream()
                        .filter(workHistoryEntity -> workHistoryEntity.getId().equals(workHistoryDTO.getId()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException(MessageUtil.getMessage(MessageConstant.WorkHistory.WORK_HISTORY_NOT_FOUND)));
                workHistoryMapper.updateWorkHistoryEntityFromWorkHistoryDTO(workHistoryDTO, existedEntity);
            }
        });
    }

    public void mapToEducationEntities(List<EducationDTO> dtos, @MappingTarget List<EducationEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(educationDTO -> {
            if (educationDTO.getId() == null) { // if skill id null: create new skill
                if (!isValidEducationDTO(educationDTO)){
                    throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Education.INVALID_EDUCATION));
                }
                EducationEntity entity = educationMapper.toEntity(educationDTO);
                entities.add(entity);
            } else {
                EducationEntity existedEntity = entities.stream()
                        .filter(educationEntity -> educationEntity.getId().equals(educationDTO.getId()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException(MessageUtil.getMessage(MessageConstant.Education.EDUCATION_NOT_FOUND)));
                educationMapper.updateEducationEntityFromEducationDTO(educationDTO, existedEntity);
            }
        });
    }

    public void mapToCertificationEntities(List<CertificationDTO> dtos, @MappingTarget List<CertificationEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(certificationDTO -> {
            if (certificationDTO.getId() == null) { // if skill id null: create new skill
                if (!isValidCertificationDTO(certificationDTO)){
                    throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Certification.INVALID_CERTIFICATION));
                }
                CertificationEntity entity = certificationMapper.toEntity(certificationDTO);
                entities.add(entity);
            } else {
                CertificationEntity existedEntity = entities.stream()
                        .filter(certificationEntity -> certificationEntity.getId().equals(certificationDTO.getId()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException(MessageUtil.getMessage(MessageConstant.Certification.CERTIFICATION_NOT_FOUND)));
                certificationMapper.updateCertificationEntityFromCertificationDTO(certificationDTO, existedEntity);
            }
        });
    }

    public void mapToReferenceEntities(List<ReferenceDTO> dtos, @MappingTarget List<ReferenceEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(referenceDTO -> {
            if (referenceDTO.getId() == null) { // if skill id null: create new skill
                if (!isValidReferenceDTO(referenceDTO)){
                    throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Reference.INVALID_REFERENCE));
                }
                ReferenceEntity entity = referenceMapper.toEntity(referenceDTO);
                entities.add(entity);
            } else {
                ReferenceEntity existedEntity = entities.stream()
                        .filter(referenceEntity -> referenceEntity.getId().equals(referenceDTO.getId()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException(MessageUtil.getMessage(MessageConstant.Reference.REFERENCE_NOT_FOUND)));
                referenceMapper.updateReferenceEntityFromRefereceDTO(referenceDTO, existedEntity);
            }
        });
    }

    public void mapToActivityEntities(List<ActivityDTO> dtos, @MappingTarget List<ActivityEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(activityDTO -> {
            if (activityDTO.getId() == null) { // if skill id null: create new skill
                if (!isValidActivityDTO(activityDTO)){
                    throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Activity.INVALID_ACTIVITY));
                }
                ActivityEntity entity = activityMapper.toEntity(activityDTO);
                entities.add(entity);
            } else {
                ActivityEntity existedEntity = entities.stream()
                        .filter(activityEntity -> activityEntity.getId().equals(activityDTO.getId()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException(MessageUtil.getMessage(MessageConstant.Activity.ACTIVITY_NOT_FOUND)));
                activityMapper.updateActivityEntityFromActivityDTO(activityDTO, existedEntity);
            }
        });
    }

    private boolean isValidSkillDTO(SkillDTO dto) {
        return dto.getName() != null && dto.getProficiency() != null;
    }

    private boolean isValidWorkHistoryDTO(WorkHistoryDTO dto) {
        return dto.getCompany() != null
                && dto.getPosition() != null
                && dto.getDescription() != null;
    }

    private boolean isValidEducationDTO(EducationDTO dto) {
        return dto.getSubject() != null
                && dto.getSchool() != null
                && dto.getAchievement() != null;
    }

    private boolean isValidCertificationDTO(CertificationDTO dto) {
        return dto.getName() != null
                && dto.getInstitution() != null
                && dto.getCertificationLink() != null;
    }

    private boolean isValidReferenceDTO(ReferenceDTO dto) {
        return dto.getCompany() != null
                && dto.getPosition() != null
                && dto.getFullname() != null;
    }

    private boolean isValidActivityDTO(ActivityDTO dto) {
        return dto.getDescription() != null
                && dto.getOrganization() != null
                && dto.getIsCurrentActivity() != null
                && dto.getFromDate() != null
                && dto.getToDate() != null
                && dto.getFunctionTitle() != null
                && dto.getName() != null;
    }

}
