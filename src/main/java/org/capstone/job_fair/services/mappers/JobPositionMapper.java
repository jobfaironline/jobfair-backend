package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.controllers.payload.requests.CompanyJobFairRegistrationRequest;
import org.capstone.job_fair.controllers.payload.requests.CreateJobPositionRequest;
import org.capstone.job_fair.models.dtos.company.SkillTagDTO;
import org.capstone.job_fair.models.dtos.company.SubCategoryDTO;
import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.models.dtos.company.job.RegistrationJobPositionDTO;
import org.capstone.job_fair.models.entities.attendant.JobLevelEntity;
import org.capstone.job_fair.models.entities.attendant.LanguageEntity;
import org.capstone.job_fair.models.entities.company.SkillTagEntity;
import org.capstone.job_fair.models.entities.company.SubCategoryEntity;
import org.capstone.job_fair.models.entities.company.job.JobPositionEntity;
import org.capstone.job_fair.models.entities.company.job.JobTypeEntity;
import org.capstone.job_fair.models.entities.company.job.RegistrationJobPositionEntity;
import org.capstone.job_fair.models.enums.JobLevel;
import org.capstone.job_fair.models.enums.JobType;
import org.capstone.job_fair.models.enums.Language;
import org.capstone.job_fair.repositories.company.job.RegistrationJobPositionRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {SubCategoryMapper.class})
public abstract class JobPositionMapper {
    @Autowired
    private SubCategoryMapper subCategoryMapper;
    @Autowired
    private SkillTagMapper skillTagMapper;

    @Mapping(target = "language", qualifiedByName = "toJobPositionDTOLanguage")
    @Mapping(target = "level", source = "jobLevel", qualifiedByName = "toJobPositionDTOJobLevel")
    @Mapping(target = "jobType", source = "jobTypeEntity", qualifiedByName = "toJobPositionDTOJobType")
    @Mapping(target = "subCategoryDTOs", source = "categories", qualifiedByName = "toJobPositionDTOSubCategory")
    @Mapping(target = "skillTagDTOS", source = "skillTagEntities", qualifiedByName = "toJobPositionDTOSkillTag")
    @Mapping(target = "companyDTO", source = "company")
    public abstract JobPositionDTO toDTO(JobPositionEntity jobPosition);

    @Mapping(target = "language", qualifiedByName = "toJobPositionEntityLanguage")
    @Mapping(target = "jobLevel", source = "level", qualifiedByName = "toJobPositionEntityJobLevel")
    @Mapping(target = "jobTypeEntity", source = "jobType", qualifiedByName = "toJobPositionEntityJobType")
    @Mapping(target = "categories", source = "subCategoryDTOs", qualifiedByName = "toJobPositionEntitySubCategory")
    @Mapping(target = "skillTagEntities", source = "skillTagDTOS", qualifiedByName = "toJobPositionEntitySkillTag")
    public abstract JobPositionEntity toEntity(JobPositionDTO jobPositionDTO);


    @Mapping(target = "language", qualifiedByName = "toJobPositionEntityLanguage")
    @Mapping(target = "jobLevel", source = "jobLevel", qualifiedByName = "toJobPositionEntityJobLevel")
    @Mapping(target = "jobTypeEntity", source = "jobType", qualifiedByName = "toJobPositionEntityJobType")
    @Mapping(target = "categories", source = "subCategoryDTOs", qualifiedByName = "toJobPositionEntitySubCategory")
    @Mapping(target = "skillTagEntities", source = "skillTagDTOS", qualifiedByName = "toJobPositionEntitySkillTag")
    public abstract RegistrationJobPositionEntity toEntity(RegistrationJobPositionDTO jobPositionDTO);

    @Mapping(source = "preferredLanguage", target = "language")
    @Mapping(source = "companyId", target = "companyDTO.id")
    @Mapping(source = "subCategoryIds", target = "subCategoryDTOs", qualifiedByName = "fromSubCategoryIdsOfCreateJobPositionRequest")
    @Mapping(source = "skillTagIds", target = "skillTagDTOS", qualifiedByName = "fromSkillTagIdsOfCreateJobPositionRequest")
    public abstract JobPositionDTO toDTO(CreateJobPositionRequest request);

    @Named("fromSubCategoryIdsOfCreateJobPositionRequest")
    public List<SubCategoryDTO> fromSubCategoryIdsOfCreateJobPositionRequest(List<Integer> subCategoryIds){
        return subCategoryIds.stream().map(SubCategoryDTO::new).collect(Collectors.toList());
    }

    @Named("fromSkillTagIdsOfCreateJobPositionRequest")
    public List<SkillTagDTO> fromSkillTagIdsOfCreateJobPositionRequest(List<Integer> skillTagIds){
        return skillTagIds.stream().map(SkillTagDTO::new).collect(Collectors.toList());
    }

    @Named("toJobPositionDTOLanguage")
    public static Language toJobPositionDTOLanguage(LanguageEntity languageEntity) {
        return Arrays.stream(Language.values())
                .filter(language -> language.getCode().equals(languageEntity.getId()))
                .findFirst()
                .orElse(null);
    }

    @Named("toJobPositionEntityLanguage")
    public static LanguageEntity toJobPositionEntityLanguage(Language language) {
        LanguageEntity entity = new LanguageEntity();
        entity.setId(language.getCode());
        return entity;
    }

    @Named("toJobPositionDTOJobLevel")
    public static JobLevel toJobPositionDTOJobLevel(JobLevelEntity jobLevelEntity) {
        return JobLevel.values()[jobLevelEntity.getId()];
    }

    @Named("toJobPositionEntityJobLevel")
    public static JobLevelEntity toJobPositionEntityJobLevel(JobLevel jobLevel) {
        JobLevelEntity entity = new JobLevelEntity();
        entity.setId(jobLevel.ordinal());
        return entity;
    }

    @Named("toJobPositionDTOJobType")
    public static JobType toJobPositionDTOJobType(JobTypeEntity jobTypeEntity) {
        return JobType.values()[jobTypeEntity.getId()];
    }

    @Named("toJobPositionEntityJobType")
    public static JobTypeEntity toJobPositionEntityJobType(JobType jobType) {
        JobTypeEntity entity = new JobTypeEntity();
        entity.setId(jobType.ordinal());
        return entity;
    }

    @Named("toJobPositionDTOSubCategory")
    public List<SubCategoryDTO> toJobPositionDTOSubCategory(Set<SubCategoryEntity> categories) {
        return categories.stream().map(entity -> subCategoryMapper.toDTO(entity)).collect(Collectors.toList());
    }

    @Named("toJobPositionEntitySubCategory")
    public Set<SubCategoryEntity> toJobPositionEntitySubCategory(List<SubCategoryDTO> categories) {
        return categories.stream()
                .map(SubCategoryDTO::getId)
                .map(SubCategoryEntity::new)
                .collect(Collectors.toSet());
    }

    @Named("toJobPositionDTOSkillTag")
    public List<SkillTagDTO> toJobPositionDTOSkillTag(Set<SkillTagEntity> skillTagEntities) {
        return skillTagEntities.stream().map(entity -> skillTagMapper.toDTO(entity)).collect(Collectors.toList());
    }

    @Named("toJobPositionEntitySkillTag")
    public Set<SkillTagEntity> toJobPositionEntitySkillTag(List<SkillTagDTO> categories) {
        return categories.stream()
                .map(SkillTagDTO::getId)
                .map(SkillTagEntity::new)
                .collect(Collectors.toSet());
    }
}
