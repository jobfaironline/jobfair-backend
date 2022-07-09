package org.capstone.job_fair.services.mappers.attendant.cv;

import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.attendant.cv.DraftCvRequest;
import org.capstone.job_fair.controllers.payload.requests.attendant.cv.UpdateCvRequest;
import org.capstone.job_fair.models.dtos.attendant.cv.*;
import org.capstone.job_fair.models.entities.attendant.cv.*;
import org.capstone.job_fair.services.mappers.attendant.AttendantMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Mapper(componentModel = "spring",
        uses = {AttendantMapper.class, CvCertificationMapper.class, CvEducationMapper.class, CvReferenceMapper.class,
                CvSkillMapper.class, CvWorkHistoryMapper.class, CvActivityMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CvMapper {

    @Autowired
    private CvSkillMapper cvSkillMapper;

    @Autowired
    private CvActivityMapper cvActivityMapper;

    @Autowired
    private CvWorkHistoryMapper cvWorkHistoryMapper;

    @Autowired
    private CvEducationMapper cvEducationMapper;

    @Autowired
    private CvReferenceMapper cvReferenceMapper;

    @Autowired
    private CvCertificationMapper cvCertificationMapper;

    public abstract CvDTO toDTO(CvEntity entity);

    public abstract CvEntity toEntity(CvDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "attendant", ignore = true)
    public abstract CvDTO toDTO(DraftCvRequest request);

    @Mapping(target = "attendant", ignore = true)
    public abstract CvDTO toDTO(UpdateCvRequest request);

    public abstract void updateCvEntityFromCvDTO(CvDTO dto, @MappingTarget CvEntity entity);

    private boolean isValidSkillDTO(CvSkillDTO dto) {
        return dto.getName() != null && dto.getProficiency() != null;
    }

    private boolean isValidWorkHistoryDTO(CvWorkHistoryDTO dto) {
        return dto.getCompany() != null
                && dto.getPosition() != null
                && dto.getDescription() != null;
    }

    private boolean isValidEducationDTO(CvEducationDTO dto) {
        return dto.getSubject() != null
                && dto.getSchool() != null
                && dto.getAchievement() != null;
    }

    private boolean isValidCertificationDTO(CvCertificationDTO dto) {
        return dto.getName() != null
                && dto.getInstitution() != null
                && dto.getCertificationLink() != null;
    }

    private boolean isValidReferenceDTO(CvReferenceDTO dto) {
        return dto.getCompany() != null
                && dto.getPosition() != null
                && dto.getFullName() != null;
    }

    private boolean isValidActivityDTO(CvActivityDTO dto) {
        return dto.getDescription() != null
                && dto.getOrganization() != null
                && dto.getIsCurrentActivity() != null
                && dto.getFromDate() != null
                && dto.getToDate() != null
                && dto.getFunctionTitle() != null
                && dto.getName() != null;
    }

    public void mapToSkillEntities(List<CvSkillDTO> dtos, @MappingTarget List<CvSkillEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(skillDTO -> {
            if (skillDTO.getId() == null) { // if id null: create new entity
                if (!isValidSkillDTO(skillDTO)) {
                    throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Skill.INVALID_SKILL));
                }
                CvSkillEntity entity = cvSkillMapper.toEntity(skillDTO);
                entities.add(entity);
            } else {
                CvSkillEntity existedEntity = entities.stream().filter(skillEntity -> Objects.equals(skillEntity.getId(), skillDTO.getId()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException(MessageUtil.getMessage(MessageConstant.Skill.SKILL_NOT_FOUND)));
                cvSkillMapper.updateCvSkillEntityFromCvSkillDTO(skillDTO, existedEntity);
            }
        });
        //remove entities that are not inside dtos
        entities.removeIf(entity -> {
                    if (entity.getId() == null) return false;
                    return dtos.stream().noneMatch(dto -> entity.getId().equals(dto.getId()));
                }
        );
    }

    public void mapToWorkHistoryEntities(List<CvWorkHistoryDTO> dtos, @MappingTarget List<CvWorkHistoryEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(workHistoryDTO -> {
            if (workHistoryDTO.getId() == null) { // if id null: create new entity
                if (!isValidWorkHistoryDTO(workHistoryDTO)) {
                    throw new IllegalArgumentException(MessageUtil.getMessage((MessageConstant.WorkHistory.INVALID_WORK_HISTORY)));
                }
                CvWorkHistoryEntity entity = cvWorkHistoryMapper.toEntity(workHistoryDTO);
                entities.add(entity);
            } else {
                CvWorkHistoryEntity existedEntity = entities.stream()
                        .filter(workHistoryEntity -> workHistoryEntity.getId().equals(workHistoryDTO.getId()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException(MessageUtil.getMessage(MessageConstant.WorkHistory.WORK_HISTORY_NOT_FOUND)));
                cvWorkHistoryMapper.updateCvWorkHistoryEntityFromCvWorkHistoryDTO(workHistoryDTO, existedEntity);
            }
        });
        //remove entities that are not inside dtos
        entities.removeIf(entity -> {
                    if (entity.getId() == null) return false;
                    return dtos.stream().noneMatch(dto -> entity.getId().equals(dto.getId()));
                }
        );
    }

    public void mapToEducationEntities(List<CvEducationDTO> dtos, @MappingTarget List<CvEducationEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(educationDTO -> {
            if (educationDTO.getId() == null) {  // if id null: create new entity
                if (!isValidEducationDTO(educationDTO)) {
                    throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Education.INVALID_EDUCATION));
                }
                CvEducationEntity entity = cvEducationMapper.toEntity(educationDTO);
                entities.add(entity);
            } else {
                CvEducationEntity existedEntity = entities.stream()
                        .filter(educationEntity -> educationEntity.getId().equals(educationDTO.getId()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException(MessageUtil.getMessage(MessageConstant.Education.EDUCATION_NOT_FOUND)));
                cvEducationMapper.updateCvEducationEntityFromCvEducationDTO(educationDTO, existedEntity);
            }
        });
        //remove entities that are not inside dtos
        entities.removeIf(entity -> {
                    if (entity.getId() == null) return false;
                    return dtos.stream().noneMatch(dto -> entity.getId().equals(dto.getId()));
                }
        );
    }

    public void mapToCertificationEntities(List<CvCertificationDTO> dtos, @MappingTarget List<CvCertificationEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(certificationDTO -> {
            if (certificationDTO.getId() == null) { // if id null: create new entity
                if (!isValidCertificationDTO(certificationDTO)) {
                    throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Certification.INVALID_CERTIFICATION));
                }
                CvCertificationEntity entity = cvCertificationMapper.toEntity(certificationDTO);
                entities.add(entity);
            } else {
                CvCertificationEntity existedEntity = entities.stream()
                        .filter(certificationEntity -> certificationEntity.getId().equals(certificationDTO.getId()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException(MessageUtil.getMessage(MessageConstant.Certification.CERTIFICATION_NOT_FOUND)));
                cvCertificationMapper.updateCvCertificationEntityFromCvCertificationDTO(certificationDTO, existedEntity);
            }
        });
        //remove entities that are not inside dtos
        entities.removeIf(entity -> {
                    if (entity.getId() == null) return false;
                    return dtos.stream().noneMatch(dto -> entity.getId().equals(dto.getId()));
                }
        );
    }

    public void mapToReferenceEntities(List<CvReferenceDTO> dtos, @MappingTarget List<CvReferenceEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(referenceDTO -> {
            if (referenceDTO.getId() == null) { // if id null: create new entity
                if (!isValidReferenceDTO(referenceDTO)) {
                    throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Reference.INVALID_REFERENCE));
                }
                CvReferenceEntity entity = cvReferenceMapper.toEntity(referenceDTO);
                entities.add(entity);
            } else {
                CvReferenceEntity existedEntity = entities.stream()
                        .filter(referenceEntity -> referenceEntity.getId().equals(referenceDTO.getId()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException(MessageUtil.getMessage(MessageConstant.Reference.REFERENCE_NOT_FOUND)));
                cvReferenceMapper.updateCvReferenceEntityFromCvReferenceDTO(referenceDTO, existedEntity);
            }
        });
        //remove entities that are not inside dtos
        entities.removeIf(entity -> {
                    if (entity.getId() == null) return false;
                    return dtos.stream().noneMatch(dto -> entity.getId().equals(dto.getId()));
                }
        );
    }

    public void mapToActivityEntities(List<CvActivityDTO> dtos, @MappingTarget List<CvActivityEntity> entities) {
        if (dtos == null) return;
        dtos.forEach(activityDTO -> {
            if (activityDTO.getId() == null) { // if id null: create new entity
                if (!isValidActivityDTO(activityDTO)) {
                    throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Activity.INVALID_ACTIVITY));
                }
                CvActivityEntity entity = cvActivityMapper.toEntity(activityDTO);
                entities.add(entity);
            } else {
                CvActivityEntity existedEntity = entities.stream()
                        .filter(activityEntity -> Objects.equals(activityEntity.getId(), activityDTO.getId()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException(MessageUtil.getMessage(MessageConstant.Activity.ACTIVITY_NOT_FOUND)));
                cvActivityMapper.updateCvActivityEntityFromCvActivityDTO(activityDTO, existedEntity);
            }
        });
        //remove entities that are not inside dtos
        entities.removeIf(entity -> {
                    if (entity.getId() == null) return false;
                    return dtos.stream().noneMatch(dto -> entity.getId().equals(dto.getId()));
                }
        );
    }
}
