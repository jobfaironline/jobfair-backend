package org.capstone.job_fair.services.mappers.company;

import org.capstone.job_fair.models.dtos.company.SkillTagDTO;
import org.capstone.job_fair.models.dtos.company.SubCategoryDTO;
import org.capstone.job_fair.models.dtos.company.job.RegistrationJobPositionDTO;
import org.capstone.job_fair.models.entities.attendant.JobLevelEntity;
import org.capstone.job_fair.models.entities.attendant.LanguageEntity;
import org.capstone.job_fair.models.entities.company.SkillTagEntity;
import org.capstone.job_fair.models.entities.company.SubCategoryEntity;
import org.capstone.job_fair.models.entities.company.job.JobTypeEntity;
import org.capstone.job_fair.models.entities.company.job.RegistrationJobPositionEntity;
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
        uses = {SubCategoryMapper.class, CompanyRegistrationMapper.class})
public abstract class RegistrationJobPositionMapper {

    @Autowired
    private SubCategoryMapper subCategoryMapper;

    @Autowired
    private SkillTagMapper skillTagMapper;


    @Mapping(target = "language", qualifiedByName = "toJobPositionDTOLanguage")
    @Mapping(target = "jobLevel", qualifiedByName = "toJobPositionDTOJobLevel")
    @Mapping(target = "jobType", source = "jobTypeEntity", qualifiedByName = "toJobPositionDTOJobType")
    @Mapping(target = "subCategoryDTOs", source="categories", qualifiedByName = "toJobPositionDTOSubCategory")
    @Mapping(target = "skillTagDTOS", source="skillTagEntities", qualifiedByName = "toJobPositionDTOSkillTag")
    @Mapping(target = "companyRegistration", ignore = true)
    public abstract RegistrationJobPositionDTO toDTO(RegistrationJobPositionEntity entity);

    @Mapping(target = "language", qualifiedByName = "toJobPositionEntityLanguage")
    @Mapping(target = "jobLevel", qualifiedByName = "toJobPositionEntityJobLevel")
    @Mapping(target = "jobTypeEntity", source = "jobType", qualifiedByName = "toJobPositionEntityJobType")
    @Mapping(target = "categories", source = "subCategoryDTOs", qualifiedByName = "toJobPositionEntitySubCategory")
    @Mapping(target = "skillTagEntities", source = "skillTagDTOS", qualifiedByName = "toJobPositionEntitySkillTag")
    @Mapping(target = "companyRegistration", ignore = true)
    public abstract RegistrationJobPositionEntity toEntity(RegistrationJobPositionDTO jobPositionDTO);

    @Named("toJobPositionDTOSkillTag")
    public List<SkillTagDTO> toJobPositionDTOSkillTag(Set<SkillTagEntity> skillTags) {
        return skillTags.stream().map(skillTagMapper::toDTO).collect(Collectors.toList());
    }

    @Named("toJobPositionDTOSubCategory")
    public List<SubCategoryDTO> toJobPositionDTOSubCategory(Set<SubCategoryEntity> categories) {
        return categories.stream().map(subCategoryMapper::toDTO).collect(Collectors.toList());
    }


    @Named("toJobPositionDTOJobType")
    public JobType toJobPositionDTOJobType(JobTypeEntity entity) {
        return JobType.values()[entity.getId()];
    }

    @Named("toJobPositionDTOJobLevel")
    public JobLevel toJobPositionJobLevel(JobLevelEntity entity) {
        return JobLevel.values()[entity.getId()];
    }

    @Named("toJobPositionDTOLanguage")
    public Language toJobPositionDTOLanguage(LanguageEntity entity) {
        return Arrays.stream(Language.values()).filter(language -> language.getCode().equals(entity.getId())).findFirst().orElse(null);
    }


    @Named("toJobPositionEntityLanguage")
    public LanguageEntity toJobPositionEntityLanguage(Language language) {
        LanguageEntity entity = new LanguageEntity();
        entity.setId(language.getCode());
        return entity;
    }

    @Named("toJobPositionEntityJobLevel")
    public JobLevelEntity toJobPositionEntityJobLevel(JobLevel jobLevel) {
        JobLevelEntity entity = new JobLevelEntity();
        entity.setId(jobLevel.ordinal());
        return entity;
    }

    @Named("toJobPositionEntityJobType")
    public JobTypeEntity toJobPositionEntityJobType(JobType jobType) {
        JobTypeEntity entity = new JobTypeEntity();
        entity.setId(jobType.ordinal());
        return entity;
    }

    @Named("toJobPositionEntitySubCategory")
    public Set<SubCategoryEntity> toJobPositionEntitySubCategory(List<SubCategoryDTO> categories) {
        return categories.stream()
                .map(SubCategoryDTO::getId)
                .map(SubCategoryEntity::new)
                .collect(Collectors.toSet());
    }

    @Named("toJobPositionEntitySkillTag")
    public Set<SkillTagEntity> toJobPositionEntitySkillTag(List<SkillTagDTO> categories) {
        return categories.stream()
                .map(SkillTagDTO::getId)
                .map(SkillTagEntity::new)
                .collect(Collectors.toSet());
    }

}
