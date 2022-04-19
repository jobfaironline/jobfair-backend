package org.capstone.job_fair.services.mappers.company;

import org.capstone.job_fair.models.dtos.company.SkillTagDTO;
import org.capstone.job_fair.models.dtos.company.SubCategoryDTO;
import org.capstone.job_fair.models.dtos.company.job.BoothJobPositionDTO;
import org.capstone.job_fair.models.entities.attendant.JobLevelEntity;
import org.capstone.job_fair.models.entities.attendant.LanguageEntity;
import org.capstone.job_fair.models.entities.company.SkillTagEntity;
import org.capstone.job_fair.models.entities.company.SubCategoryEntity;
import org.capstone.job_fair.models.entities.company.job.JobTypeEntity;
import org.capstone.job_fair.models.entities.company.job.BoothJobPositionEntity;
import org.capstone.job_fair.models.enums.JobLevel;
import org.capstone.job_fair.models.enums.JobType;
import org.capstone.job_fair.models.enums.Language;
import org.capstone.job_fair.services.mappers.job_fair.SkillTagMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {SubCategoryMapper.class})
public abstract class BoothJobPositionMapper {

    @Autowired
    private SubCategoryMapper subCategoryMapper;

    @Autowired
    private SkillTagMapper skillTagMapper;


    @Mapping(target = "language", qualifiedByName = "toJobPositionDTOLanguage")
    @Mapping(target = "jobLevel", qualifiedByName = "toJobPositionDTOJobLevel")
    @Mapping(target = "jobType", source = "jobTypeEntity", qualifiedByName = "toJobPositionDTOJobType")
    @Mapping(target = "subCategoryDTOs", source = "categories", qualifiedByName = "toJobPositionDTOSubCategory")
    @Mapping(target = "skillTagDTOS", source = "skillTagEntities", qualifiedByName = "toJobPositionDTOSkillTag")
    @Mapping(target = "jobFairBooth", ignore = true)
    public abstract BoothJobPositionDTO toDTO(BoothJobPositionEntity entity);

    @Mapping(target = "language", qualifiedByName = "toJobPositionEntityLanguage")
    @Mapping(target = "jobLevel", qualifiedByName = "toJobPositionEntityJobLevel")
    @Mapping(target = "jobTypeEntity", source = "jobType", qualifiedByName = "toJobPositionEntityJobType")
    @Mapping(target = "categories", source = "subCategoryDTOs", qualifiedByName = "toJobPositionEntitySubCategory")
    @Mapping(target = "skillTagEntities", source = "skillTagDTOS", qualifiedByName = "toJobPositionEntitySkillTag")
    @Mapping(target = "jobFairBooth", ignore = true)
    public abstract BoothJobPositionEntity toEntity(BoothJobPositionDTO jobPositionDTO);

    @Named("toJobPositionDTOSkillTag")
    public List<SkillTagDTO> toJobPositionDTOSkillTag(Set<SkillTagEntity> skillTags) {
        if (skillTags == null) return null;
        return skillTags.stream().map(skillTagMapper::toDTO).collect(Collectors.toList());
    }

    @Named("toJobPositionDTOSubCategory")
    public List<SubCategoryDTO> toJobPositionDTOSubCategory(Set<SubCategoryEntity> categories) {
        if (categories == null) return null;
        return categories.stream().map(subCategoryMapper::toDTO).collect(Collectors.toList());
    }


    @Named("toJobPositionDTOJobType")
    public JobType toJobPositionDTOJobType(JobTypeEntity entity) {
        if (entity == null) return null;
        return JobType.values()[entity.getId()];
    }

    @Named("toJobPositionDTOJobLevel")
    public JobLevel toJobPositionJobLevel(JobLevelEntity entity) {
        if (entity == null) return null;
        return JobLevel.values()[entity.getId()];
    }

    @Named("toJobPositionDTOLanguage")
    public Language toJobPositionDTOLanguage(LanguageEntity entity) {
        if (entity == null) return null;
        return Arrays.stream(Language.values()).filter(language -> language.getCode().equals(entity.getId())).findFirst().orElse(null);
    }


    @Named("toJobPositionEntityLanguage")
    public LanguageEntity toJobPositionEntityLanguage(Language language) {
        if (language == null) return null;
        LanguageEntity entity = new LanguageEntity();
        entity.setId(language.getCode());
        return entity;
    }

    @Named("toJobPositionEntityJobLevel")
    public JobLevelEntity toJobPositionEntityJobLevel(JobLevel jobLevel) {
        if (jobLevel == null) return null;
        JobLevelEntity entity = new JobLevelEntity();
        entity.setId(jobLevel.ordinal());
        return entity;
    }

    @Named("toJobPositionEntityJobType")
    public JobTypeEntity toJobPositionEntityJobType(JobType jobType) {
        if (jobType == null) return null;
        JobTypeEntity entity = new JobTypeEntity();
        entity.setId(jobType.ordinal());
        return entity;
    }

    @Named("toJobPositionEntitySubCategory")
    public Set<SubCategoryEntity> toJobPositionEntitySubCategory(List<SubCategoryDTO> categories) {
        if (categories == null) return null;
        return categories.stream()
                .map(SubCategoryDTO::getId)
                .map(SubCategoryEntity::new)
                .collect(Collectors.toSet());
    }

    @Named("toJobPositionEntitySkillTag")
    public Set<SkillTagEntity> toJobPositionEntitySkillTag(List<SkillTagDTO> categories) {
        if (categories == null) return null;
        return categories.stream()
                .map(SkillTagDTO::getId)
                .map(SkillTagEntity::new)
                .collect(Collectors.toSet());
    }

}
