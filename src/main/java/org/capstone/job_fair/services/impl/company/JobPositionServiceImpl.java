package org.capstone.job_fair.services.impl.company;

import lombok.AllArgsConstructor;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.models.entities.company.SkillTagEntity;
import org.capstone.job_fair.models.entities.company.job.JobPositionEntity;
import org.capstone.job_fair.repositories.company.SkillTagRepository;
import org.capstone.job_fair.repositories.company.SubCategoryRepository;
import org.capstone.job_fair.repositories.company.job.JobPositionRepository;
import org.capstone.job_fair.services.interfaces.company.CompanyService;
import org.capstone.job_fair.services.interfaces.company.JobPositionService;
import org.capstone.job_fair.services.mappers.JobPositionMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class JobPositionServiceImpl implements JobPositionService {
    @Autowired
    private JobPositionRepository jobPositionRepository;
    @Autowired
    private JobPositionMapper mapper;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private SkillTagRepository skillTagRepository;

    private boolean isSubCategoryIdValid(int id) {
        return subCategoryRepository.existsById(id);
    }

    private boolean isSkillTagIdValid(int id){
        return skillTagRepository.existsById(id);
    }

    @Override
    public void createNewJobPosition(JobPositionDTO dto) {
        if (dto.getSubCategoryDTOs() != null){
            dto.getSubCategoryDTOs().forEach(subCategoryDTO -> {
                if (!isSubCategoryIdValid(subCategoryDTO.getId())){
                    throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.SubCategory.NOT_FOUND));
                }
            });
        }
        if (dto.getSkillTagDTOS() != null){
            dto.getSkillTagDTOS().forEach(skillTagDTO -> {
                if (!isSkillTagIdValid(skillTagDTO.getId())){
                    throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.SkillTag.NOT_FOUND));
                }
            });
        }
        if (companyService.getCountById(dto.getCompanyDTO().getId()) == 0) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Company.NOT_FOUND));
        }

        JobPositionEntity entity = mapper.toEntity(dto);
        jobPositionRepository.save(entity);
    }

    @Override
    public JobPositionEntity getJobByID(String jobPositionId) {
        JobPositionEntity jobPosition =  jobPositionRepository.getById(jobPositionId);
        if(jobPosition == null) throw new IllegalArgumentException(
                MessageUtil.getMessage(MessageConstant.Job.JOB_POSITION_NOT_FOUND));
        return jobPosition;
    }
}
