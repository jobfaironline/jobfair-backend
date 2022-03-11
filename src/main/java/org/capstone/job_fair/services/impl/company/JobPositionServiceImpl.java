package org.capstone.job_fair.services.impl.company;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.capstone.job_fair.constants.CSVConstant;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.company.CreateJobPositionRequest;
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
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

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
    @Autowired
    private Validator validator;

    private boolean isSubCategoryIdValid(int id) {
        return subCategoryRepository.existsById(id);
    }

    private boolean isSkillTagIdValid(int id) {
        return skillTagRepository.existsById(id);
    }

    private void createNewJobPositionPrivate(JobPositionDTO dto) {

    }

    @Override
    @Transactional
    public JobPositionDTO createNewJobPosition(JobPositionDTO dto) {
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
        long currentTime = new Date().getTime();
        dto.setCreatedDate(currentTime);
        JobPositionEntity entity = mapper.toEntity(dto);
        entity = jobPositionRepository.save(entity);
        return mapper.toDTO(entity);
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
        long currentTime = new Date().getTime();
        dto.setUpdateDate(currentTime);
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

    @SneakyThrows
    @Override
    @Transactional
    public List<JobPositionDTO> createNewJobPositionsFromCSVFile(MultipartFile file) {
        List<JobPositionDTO> result = new ArrayList<>();
        if (!CSVConstant.TYPE.equals(file.getContentType())) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.CSV_FILE_ERROR));
        }
        Reader reader = new InputStreamReader(file.getInputStream());
        CsvToBean<CreateJobPositionRequest> csvToBean = new CsvToBeanBuilder(reader)
                .withType(CreateJobPositionRequest.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        Iterator<CreateJobPositionRequest> jobsCSV = csvToBean.iterator();
        int numberOfCreatedJob = 0;
        while (jobsCSV.hasNext()) {
            numberOfCreatedJob++;
            CreateJobPositionRequest jobRequest = jobsCSV.next();
            Errors errors = new BindException(jobRequest, CreateJobPositionRequest.class.getSimpleName());
            validator.validate(jobRequest, errors);
            if (errors.hasErrors()) {
                String errorMessage = String.format(MessageUtil.getMessage(MessageConstant.Job.CSV_LINE_ERROR), numberOfCreatedJob);
                throw new IllegalArgumentException(errorMessage);
            }
            JobPositionDTO jobPositionDTO = mapper.toDTO(jobRequest);
            jobPositionDTO = createNewJobPosition(jobPositionDTO);
            result.add(jobPositionDTO);
        }
        return result;
    }


}
