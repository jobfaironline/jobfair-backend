package org.capstone.job_fair.services.mappers;

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
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {SubCategoryMapper.class})
public abstract class RegistrationJobPositionMapper {

//    @Mapping(target = "language", qualifiedByName = "toJobPositionEntityLanguage")
//    @Mapping(target = "jobLevel", source = "jobLevel", qualifiedByName = "toJobPositionEntityJobLevel")
//    @Mapping(target = "jobTypeEntity", source = "jobType", qualifiedByName = "toJobPositionEntityJobType")
//    @Mapping(target = "categories", source = "subCategoryDTOs", qualifiedByName = "toJobPositionEntitySubCategory")
//    @Mapping(target = "skillTagEntities", source = "skillTagDTOS", qualifiedByName = "toJobPositionEntitySkillTag")
    public abstract RegistrationJobPositionEntity toEntity(RegistrationJobPositionDTO jobPositionDTO);

    @Named("toJobPositionEntityLanguage")
    public static LanguageEntity toJobPositionEntityLanguage(Language language) {
        LanguageEntity entity = new LanguageEntity();
        entity.setId(language.getCode());
        return entity;
    }

    @Named("toJobPositionEntityJobLevel")
    public static JobLevelEntity toJobPositionEntityJobLevel(JobLevel jobLevel) {
        JobLevelEntity entity = new JobLevelEntity();
        entity.setId(jobLevel.ordinal());
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

    @Named("toJobPositionEntityJobType")
    public static JobTypeEntity toJobPositionEntityJobType(JobType jobType) {
        JobTypeEntity entity = new JobTypeEntity();
        entity.setId(jobType.ordinal());
        return entity;
    }

}
