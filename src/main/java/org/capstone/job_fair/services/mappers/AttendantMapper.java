package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.UpdateAttendantRequest;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.*;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.capstone.job_fair.models.entities.attendant.CountryEntity;
import org.capstone.job_fair.models.entities.attendant.JobLevelEntity;
import org.capstone.job_fair.models.entities.attendant.ResidenceEntity;
import org.capstone.job_fair.models.entities.attendant.cv.*;
import org.capstone.job_fair.models.enums.JobLevel;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.NoSuchElementException;

@Mapper(componentModel = "spring", uses = {AccountEntityMapper.class, SkillMapper.class, WorkHistoryMapper.class, EducationMapper.class, CertificationMapper.class, ReferenceMapper.class, ActivityMapper.class},
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

    @Named("fromCountryIdOfAttendantDTO")
    public CountryEntity fromCountryIdOfAttendantDTO(String countryId) {
        if (countryId == null) return null;
        CountryEntity entity = new CountryEntity();
        entity.setId(countryId);
        return entity;
    }

    @Named("fromResidenceIdOfAttendantDTO")
    public ResidenceEntity fromResidenceIdOfAttendantDTO(String residenceId) {
        if (residenceId == null) return null;
        ResidenceEntity entity = new ResidenceEntity();
        entity.setId(residenceId);
        return entity;
    }

    public void mapToSkillEntities(List<SkillDTO> dtos, @MappingTarget List<SkillEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(skillDTO -> {
            if (skillDTO.getId() == null) { // if skill id null: create new skill
                SkillEntity entity = skillMapper.toEntity(skillDTO);
                entities.add(entity);
            } else {
                SkillEntity existedEntity = entities.stream().filter(skillEntity -> skillEntity.getId().equals(skillDTO.getId()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException(MessageConstant.Skill.SKILL_NOT_FOUND));
                skillMapper.updateSkillEntityFromSkillDTO(skillDTO, existedEntity);
            }
        });
    }

    public void mapToWorkHistoryEntities(List<WorkHistoryDTO> dtos, @MappingTarget List<WorkHistoryEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(workHistoryDTO -> {
            if (workHistoryDTO.getId() == null) { // if skill id null: create new skill
                WorkHistoryEntity entity = workHistoryMapper.toEntity(workHistoryDTO);
                entities.add(entity);
            } else {
                WorkHistoryEntity existedEntity = entities.stream()
                        .filter(workHistoryEntity -> workHistoryEntity.getId().equals(workHistoryDTO.getId()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException(MessageConstant.WorkHistory.WORK_HISTORY_NOT_FOUND));
                workHistoryMapper.updateWorkHistoryEntityFromWorkHistoryDTO(workHistoryDTO, existedEntity);
            }
        });
    }

    public void mapToEducationEntities(List<EducationDTO> dtos, @MappingTarget List<EducationEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(educationDTO -> {
            if (educationDTO.getId() == null) { // if skill id null: create new skill
                EducationEntity entity = educationMapper.toEntity(educationDTO);
                entities.add(entity);
            } else {
                EducationEntity existedEntity = entities.stream()
                        .filter(educationEntity -> educationEntity.getId().equals(educationDTO.getId()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException(MessageConstant.Education.EDUCATION_NOT_FOUND));
                educationMapper.updateEducationEntityFromEducationDTO(educationDTO, existedEntity);
            }
        });
    }

    public void mapToCertificationEntities(List<CertificationDTO> dtos, @MappingTarget List<CertificationEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(certificationDTO -> {
            if (certificationDTO.getId() == null) { // if skill id null: create new skill
                CertificationEntity entity = certificationMapper.toEntity(certificationDTO);
                entities.add(entity);
            } else {
                CertificationEntity existedEntity = entities.stream()
                        .filter(certificationEntity -> certificationEntity.getId().equals(certificationDTO.getId()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException(MessageConstant.Certification.CERTIFICATION_NOT_FOUND));
                certificationMapper.updateCertificationEntityFromCertificationDTO(certificationDTO, existedEntity);
            }
        });
    }

    public void mapToReferenceEntities(List<ReferenceDTO> dtos, @MappingTarget List<ReferenceEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(referenceDTO -> {
            if (referenceDTO.getId() == null) { // if skill id null: create new skill
                ReferenceEntity entity = referenceMapper.toEntity(referenceDTO);
                entities.add(entity);
            } else {
                ReferenceEntity existedEntity = entities.stream()
                        .filter(referenceEntity -> referenceEntity.getId().equals(referenceDTO.getId()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException(MessageConstant.Reference.REFERENCE_NOT_FOUND));
                referenceMapper.updateReferenceEntityFromRefereceDTO(referenceDTO, existedEntity);
            }
        });
    }

    public void mapToActivityEntities(List<ActivityDTO> dtos, @MappingTarget List<ActivityEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(activityDTO -> {
            if (activityDTO.getId() == null) { // if skill id null: create new skill
                ActivityEntity entity = activityMapper.toEntity(activityDTO);
                entities.add(entity);
            } else {
                ActivityEntity existedEntity = entities.stream()
                        .filter(activityEntity -> activityEntity.getId().equals(activityDTO.getId()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException(MessageConstant.Activity.ACTIVITY_NOT_FOUND));
                activityMapper.updateActivityEntityFromActivityDTO(activityDTO, existedEntity);
            }
        });
    }


}
