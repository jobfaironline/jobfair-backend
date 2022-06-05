package org.capstone.job_fair.services.mappers.attendant;

import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.attendant.RegisterAttendantRequest;
import org.capstone.job_fair.controllers.payload.requests.attendant.UpdateAttendantRequest;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.dtos.attendant.profile.*;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.capstone.job_fair.models.entities.attendant.misc.CountryEntity;
import org.capstone.job_fair.models.entities.attendant.misc.ResidenceEntity;
import org.capstone.job_fair.models.entities.attendant.profile.*;
import org.capstone.job_fair.services.mappers.account.AccountMapper;
import org.capstone.job_fair.services.mappers.attendant.profile.*;
import org.capstone.job_fair.utils.MessageUtil;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

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
    public abstract AttendantDTO toDTO(UpdateAttendantRequest request);


    @Mapping(source = "password", target = "account.password")
    @Mapping(source = "email", target = "account.email")
    @Mapping(source = "phone", target = "account.phone")
    @Mapping(source = "firstname", target = "account.firstname")
    @Mapping(source = "lastname", target = "account.lastname")
    @Mapping(source = "middlename", target = "account.middlename")
    @Mapping(target = "account.gender", ignore = true)
    @Mapping(target = "title", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "dob", ignore = true)
    @Mapping(target = "jobTitle", ignore = true)
    @Mapping(target = "yearOfExp", ignore = true)
    @Mapping(target = "maritalStatus", ignore = true)
    @Mapping(target = "countryId", ignore = true)
    @Mapping(target = "residenceId", ignore = true)
    @Mapping(target = "jobLevel", ignore = true)
    @Mapping(target = "skills", ignore = true)
    @Mapping(target = "workHistories", ignore = true)
    @Mapping(target = "educations", ignore = true)
    @Mapping(target = "certifications", ignore = true)
    @Mapping(target = "references", ignore = true)
    @Mapping(target = "activities", ignore = true)
    public abstract AttendantDTO toDTO(RegisterAttendantRequest request);

    @Named("fromCountryIdOfAttendantDTO")
    public CountryEntity fromCountryIdOfAttendantDTO(Integer countryId) {
        if (countryId == null) return null;
        CountryEntity entity = new CountryEntity();
        entity.setId(countryId);
        return entity;
    }

    @Named("fromCountryEntityOfAttendantEntity")
    public Integer fromCountryEntityOfAttendantEntity(CountryEntity entity) {
        if (entity == null) return null;
        return entity.getId();
    }

    @Named("fromResidenceIdOfAttendantDTO")
    public ResidenceEntity fromResidenceIdOfAttendantDTO(Integer residenceId) {
        if (residenceId == null) return null;
        ResidenceEntity entity = new ResidenceEntity();
        entity.setId(residenceId);
        return entity;
    }

    @Named("fromResidenceEntityOfAttendantEntity")
    public Integer fromResidenceEntityOfAttendantEntity(ResidenceEntity entity) {
        if (entity == null) return null;
        return entity.getId();
    }

    public void mapToSkillEntities(List<SkillDTO> dtos, @MappingTarget List<SkillEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(skillDTO -> {
            if (skillDTO.getId() == null) { // if id null: create new entity
                if (!isValidSkillDTO(skillDTO)) {
                    throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Skill.INVALID_SKILL));
                }
                SkillEntity entity = skillMapper.toEntity(skillDTO);
                entities.add(entity);
            } else {
                SkillEntity existedEntity = entities.stream().filter(skillEntity -> Objects.equals(skillEntity.getId(), skillDTO.getId()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException(MessageUtil.getMessage(MessageConstant.Skill.SKILL_NOT_FOUND)));
                skillMapper.updateSkillEntityFromSkillDTO(skillDTO, existedEntity);
            }
        });
        //remove entities that are not inside dtos
        entities.removeIf(entity -> {
                    if (entity.getId() == null) return false;
                    return dtos.stream().noneMatch(dto -> entity.getId().equals(dto.getId()));
                }
        );
    }

    public void mapToWorkHistoryEntities(List<WorkHistoryDTO> dtos, @MappingTarget List<WorkHistoryEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(workHistoryDTO -> {
            if (workHistoryDTO.getId() == null) { // if id null: create new entity
                if (!isValidWorkHistoryDTO(workHistoryDTO)) {
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
        //remove entities that are not inside dtos
        entities.removeIf(entity -> {
                    if (entity.getId() == null) return false;
                    return dtos.stream().noneMatch(dto -> entity.getId().equals(dto.getId()));
                }
        );
    }

    public void mapToEducationEntities(List<EducationDTO> dtos, @MappingTarget List<EducationEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(educationDTO -> {
            if (educationDTO.getId() == null) {  // if id null: create new entity
                if (!isValidEducationDTO(educationDTO)) {
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
        //remove entities that are not inside dtos
        entities.removeIf(entity -> {
                    if (entity.getId() == null) return false;
                    return dtos.stream().noneMatch(dto -> entity.getId().equals(dto.getId()));
                }
        );
    }

    public void mapToCertificationEntities(List<CertificationDTO> dtos, @MappingTarget List<CertificationEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(certificationDTO -> {
            if (certificationDTO.getId() == null) { // if id null: create new entity
                if (!isValidCertificationDTO(certificationDTO)) {
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
        //remove entities that are not inside dtos
        entities.removeIf(entity -> {
                    if (entity.getId() == null) return false;
                    return dtos.stream().noneMatch(dto -> entity.getId().equals(dto.getId()));
                }
        );
    }

    public void mapToReferenceEntities(List<ReferenceDTO> dtos, @MappingTarget List<ReferenceEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(referenceDTO -> {
            if (referenceDTO.getId() == null) { // if id null: create new entity
                if (!isValidReferenceDTO(referenceDTO)) {
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
        //remove entities that are not inside dtos
        entities.removeIf(entity -> {
                    if (entity.getId() == null) return false;
                    return dtos.stream().noneMatch(dto -> entity.getId().equals(dto.getId()));
                }
        );
    }

    public void mapToActivityEntities(List<ActivityDTO> dtos, @MappingTarget List<ActivityEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(activityDTO -> {
            if (activityDTO.getId() == null) { // if id null: create new entity
                if (!isValidActivityDTO(activityDTO)) {
                    throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Activity.INVALID_ACTIVITY));
                }
                ActivityEntity entity = activityMapper.toEntity(activityDTO);
                entities.add(entity);
            } else {
                ActivityEntity existedEntity = entities.stream()
                        .filter(activityEntity -> Objects.equals(activityEntity.getId(), activityDTO.getId()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException(MessageUtil.getMessage(MessageConstant.Activity.ACTIVITY_NOT_FOUND)));
                activityMapper.updateActivityEntityFromActivityDTO(activityDTO, existedEntity);
            }
        });
        //remove entities that are not inside dtos
        entities.removeIf(entity -> {
                    if (entity.getId() == null) return false;
                    return dtos.stream().noneMatch(dto -> entity.getId().equals(dto.getId()));
                }
        );
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
