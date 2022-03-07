package org.capstone.job_fair.services.impl.company;

import lombok.AllArgsConstructor;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.models.entities.company.job.JobPositionEntity;
import org.capstone.job_fair.models.enums.JobLevel;
import org.capstone.job_fair.repositories.company.CompanyRepository;
import org.capstone.job_fair.repositories.company.SkillTagRepository;
import org.capstone.job_fair.repositories.company.SubCategoryRepository;
import org.capstone.job_fair.repositories.company.job.JobPositionRepository;
import org.capstone.job_fair.services.interfaces.company.CompanyService;
import org.capstone.job_fair.services.interfaces.company.JobPositionService;
import org.capstone.job_fair.services.mappers.company.JobPositionMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
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
    @Autowired
    private CompanyRepository companyRepository;

    private boolean isSubCategoryIdValid(int id) {
        return subCategoryRepository.existsById(id);
    }

    private boolean isSkillTagIdValid(int id) {
        return skillTagRepository.existsById(id);
    }

    @Override
    @Transactional
    public void createNewJobPosition(JobPositionDTO dto) {
        if (dto.getSubCategoryDTOs() != null) {
            dto.getSubCategoryDTOs().forEach(subCategoryDTO -> {
                if (!isSubCategoryIdValid(subCategoryDTO.getId())) {
                    throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.SubCategory.NOT_FOUND));
                }
            });
        }
        if (dto.getSkillTagDTOS() != null) {
            dto.getSkillTagDTOS().forEach(skillTagDTO -> {
                if (!isSkillTagIdValid(skillTagDTO.getId())) {
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
    @Transactional
    public JobPositionDTO updateJobPosition(JobPositionDTO dto, String companyId) {
        Optional<JobPositionEntity> jobPositionEntityOpt = jobPositionRepository.findById(dto.getId());

        if (!jobPositionEntityOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.JOB_POSITION_NOT_FOUND));
        }
        JobPositionEntity jobPositionEntity = jobPositionEntityOpt.get();

        if (!jobPositionEntity.getCompany().getId().equals(companyId))
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.COMPANY_MISMATCH));

        if (dto.getSubCategoryDTOs() != null) {
            dto.getSubCategoryDTOs().forEach(subCategoryDTO -> {
                if (!isSubCategoryIdValid(subCategoryDTO.getId())) {
                    throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.SubCategory.NOT_FOUND));
                }
            });
        }
        if (dto.getSkillTagDTOS() != null) {
            dto.getSkillTagDTOS().forEach(skillTagDTO -> {
                if (!isSkillTagIdValid(skillTagDTO.getId())) {
                    throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.SkillTag.NOT_FOUND));
                }
            });
        }
        mapper.updateJobPositionEntity(dto, jobPositionEntity);
        jobPositionRepository.save(jobPositionEntity);
        return mapper.toDTO(jobPositionEntity);
    }

    @Override
    @Transactional
    public void deleteJobPosition(String jobPositionId, String companyId) {
        Optional<JobPositionEntity> jobPositionEntityOpt = jobPositionRepository.findById(jobPositionId);

        if (!jobPositionEntityOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.JOB_POSITION_NOT_FOUND));
        }
        JobPositionEntity jobPositionEntity = jobPositionEntityOpt.get();

        if (!jobPositionEntity.getCompany().getId().equals(companyId))
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.COMPANY_MISMATCH));

        jobPositionRepository.delete(jobPositionEntity);
    }

    @Override
    public Page<JobPositionDTO> getAllJobPositionOfCompany(String companyId, Integer jobTypeId, JobLevel jobLevel, int pageSize, int offset, String sortBy, Sort.Direction direction) {
        //Check for company existence
        Optional<CompanyEntity> companyOpt = companyRepository.findById(companyId);
        if (!companyOpt.isPresent()) throw new
                IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Company.NOT_FOUND));
        if (offset < DataConstraint.Paging.OFFSET_MIN || pageSize < DataConstraint.Paging.PAGE_SIZE_MIN)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.INVALID_PAGE_NUMBER));
        Integer jobLevelId = null;
        if (jobLevelId != null) jobLevel.ordinal();
        Page<JobPositionEntity> jobPositionEntities = jobPositionRepository.findAllByCriteria(companyId, jobTypeId, jobLevelId, PageRequest.of(offset, pageSize).withSort(Sort.by(direction, sortBy)));
        return jobPositionEntities.map(entity -> mapper.toDTO(entity));
    }


}
