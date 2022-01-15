package org.capstone.job_fair.services.impl.company;

import lombok.AllArgsConstructor;
import org.capstone.job_fair.models.dtos.company.SkillTagDTO;
import org.capstone.job_fair.models.dtos.company.SubCategoryDTO;
import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.models.entities.attendant.JobLevelEntity;
import org.capstone.job_fair.models.entities.attendant.LanguageEntity;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.models.entities.company.SkillTagEntity;
import org.capstone.job_fair.models.entities.company.SubCategoryEntity;
import org.capstone.job_fair.models.entities.company.job.JobPositionEntity;
import org.capstone.job_fair.models.entities.company.job.JobTypeEntity;
import org.capstone.job_fair.repositories.company.job.JobPositionRepository;
import org.capstone.job_fair.services.interfaces.company.JobPositionService;
import org.capstone.job_fair.services.mappers.JobPositionEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class JobPositionServiceImpl implements JobPositionService {
    @Autowired
    private JobPositionRepository jobPositionRepository;
    @Autowired
    private JobPositionEntityMapper mapper;

    @Override
    public void createNewJobPosition(JobPositionDTO dto) {
        String id = UUID.randomUUID().toString();
        JobPositionEntity entity = mapper.toEntity(dto);
        entity.setId(id);

        LanguageEntity languageEntity = new LanguageEntity();
        languageEntity.setId(dto.getLanguage().getCode());

        JobLevelEntity jobLevelEntity = new JobLevelEntity();
        jobLevelEntity.setId(dto.getLevel().ordinal());

        JobTypeEntity jobTypeEntity = new JobTypeEntity();
        jobTypeEntity.setId(dto.getJobType().ordinal());

        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setId(dto.getComapnyId());

        List<SubCategoryEntity> categoryEntities = dto.getSubCategoryDTOs().stream()
                .map(SubCategoryDTO::getId)
                .map(SubCategoryEntity::new)
                .collect(Collectors.toList());

        List<SkillTagEntity> skillTagEntities = dto.getSkillTagDTOS().stream()
                .map(SkillTagDTO::getId)
                .map(SkillTagEntity::new)
                .collect(Collectors.toList());

        entity.setCompany(companyEntity);
        entity.setLanguage(languageEntity);
        entity.setJobTypeEntity(jobTypeEntity);
        entity.setJobLevel(jobLevelEntity);
        entity.setCategories(categoryEntities);
        entity.setSkillTagEntities(skillTagEntities);

        jobPositionRepository.save(entity);
    }
}
