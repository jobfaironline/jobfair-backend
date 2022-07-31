package org.capstone.job_fair.services.impl.company.job;

import com.amazonaws.util.json.Jackson;
import lombok.SneakyThrows;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.*;
import org.capstone.job_fair.controllers.payload.requests.company.CreateJobPositionRequest;
import org.capstone.job_fair.controllers.payload.responses.KeyWordResponse;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.models.dtos.util.ParseFileResult;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.models.entities.company.job.JobPositionEntity;
import org.capstone.job_fair.models.enums.JobLevel;
import org.capstone.job_fair.models.enums.JobType;
import org.capstone.job_fair.models.enums.Language;
import org.capstone.job_fair.repositories.company.CompanyRepository;
import org.capstone.job_fair.repositories.company.job.JobPositionRepository;
import org.capstone.job_fair.repositories.company.misc.SkillTagRepository;
import org.capstone.job_fair.repositories.company.misc.SubCategoryRepository;
import org.capstone.job_fair.services.interfaces.company.CompanyService;
import org.capstone.job_fair.services.interfaces.company.job.JobPositionService;
import org.capstone.job_fair.services.interfaces.util.XSLSFileService;
import org.capstone.job_fair.services.mappers.company.job.JobPositionMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class JobPositionServiceImpl implements JobPositionService {

    @Value("${skill.processor.url}")
    private String skillProcessorURL;

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
    @Autowired
    private XSLSFileService xslsFileService;
    @Autowired
    private WebClient webClient;
    @Autowired
    private Clock clock;


    private boolean isSubCategoryIdValid(int id) {
        return subCategoryRepository.existsById(id);
    }

    private boolean isSkillTagIdValid(int id) {
        return skillTagRepository.existsById(id);
    }


    private void updateDescriptionAndRequirementKeyWork(JobPositionEntity jobPosition){
        Map<String, String> body = new HashedMap<>();

        body.put("description", jobPosition.getDescription());
        Mono<KeyWordResponse> descriptionResult = webClient.post().uri(skillProcessorURL + SkillExtractorApiEndpoint.EXTRACT_KEYWORD)
                .body(Mono.just(body), Map.class)
                .retrieve()
                .bodyToMono(KeyWordResponse.class);
        descriptionResult.subscribe(keyWordResponse -> {
            try {
                String parseResult = Jackson.getObjectMapper().writeValueAsString(keyWordResponse.result);
                jobPosition.setDescriptionKeyWord(parseResult);
                jobPositionRepository.save(jobPosition);
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        body.put("description", jobPosition.getRequirements());
        Mono<KeyWordResponse> requirementResult = webClient.post().uri(skillProcessorURL+SkillExtractorApiEndpoint.EXTRACT_KEYWORD)
                .body(Mono.just(body), Map.class)
                .retrieve()
                .bodyToMono(KeyWordResponse.class);
        requirementResult.subscribe(keyWordResponse -> {
            try {
                String parseResult = Jackson.getObjectMapper().writeValueAsString(keyWordResponse.result);
                jobPosition.setRequirementKeyWord(parseResult);
                jobPositionRepository.save(jobPosition);
            } catch (Exception e){
                e.printStackTrace();
            }
        });
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
        long currentTime = clock.millis();
        dto.setCreatedDate(currentTime);

        JobPositionEntity entity = mapper.toEntity(dto);
        entity = jobPositionRepository.save(entity);
        updateDescriptionAndRequirementKeyWork(entity);



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
        long currentTime = clock.millis();
        dto.setUpdateDate(currentTime);
        mapper.updateJobPositionEntity(dto, jobPositionEntity);
        jobPositionEntity = jobPositionRepository.save(jobPositionEntity);
        this.updateDescriptionAndRequirementKeyWork(jobPositionEntity);
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
    public Page<JobPositionDTO> getAllJobPositionOfCompany(String companyId, Integer jobTypeId, JobLevel jobLevel, String jobTitle, int pageSize, int offset, String sortBy, Sort.Direction direction) {
        //Check for company existence
        Optional<CompanyEntity> companyOpt = companyRepository.findById(companyId);
        if (!companyOpt.isPresent()) throw new
                IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Company.NOT_FOUND));
        if (offset < DataConstraint.Paging.OFFSET_MIN || pageSize < DataConstraint.Paging.PAGE_SIZE_MIN)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.INVALID_PAGE_NUMBER));
        Integer jobLevelId = null;
        if (jobLevelId != null) jobLevel.ordinal();
        Page<JobPositionEntity> jobPositionEntities = jobPositionRepository.findAllByCriteria(companyId, jobTypeId, jobLevelId, jobTitle, PageRequest.of(offset, pageSize).withSort(Sort.by(direction, sortBy)));
        return jobPositionEntities.map(entity -> mapper.toDTO(entity));
    }

    private ParseFileResult<JobPositionDTO> createNewJobPositionFromListString(List<List<String>> data, String companyId) {
        ParseFileResult<JobPositionDTO> parseResult = new ParseFileResult();
        int rowNum = data.size();
        for (int i = 1; i < rowNum; i++) {
            List<String> rowData = data.get(i);

            String title = rowData.get(JobPositionConstant.XLSXFormat.TITLE_INDEX);
            String contactPersonName = rowData.get(JobPositionConstant.XLSXFormat.CONTACT_PERSON_NAME_INDEX);
            String contactEmail = rowData.get(JobPositionConstant.XLSXFormat.CONTACT_EMAIL_INDEX);
            String preferredLanguageString = rowData.get(JobPositionConstant.XLSXFormat.PREFER_LANGUAGE_INDEX);
            String levelString = rowData.get(JobPositionConstant.XLSXFormat.LEVEL_INDEX);
            String jobTypeString = rowData.get(JobPositionConstant.XLSXFormat.JOB_TYPE_INDEX);
            String locationId = rowData.get(JobPositionConstant.XLSXFormat.LOCATION_INDEX);
            String subCategoryIdsString = rowData.get(JobPositionConstant.XLSXFormat.SUB_CATEGORIES_INDEX);
            String skillTagIdsString = rowData.get(JobPositionConstant.XLSXFormat.SKILL_TAG_IDS_INDEX);
            String description = rowData.get(JobPositionConstant.XLSXFormat.DESCRIPTION);
            String requirements = rowData.get(JobPositionConstant.XLSXFormat.REQUIREMENTS);
            Language preferredLanguage;
            try {
                preferredLanguage = Language.valueOf(preferredLanguageString);
            } catch (IllegalArgumentException e) {
                parseResult.addErrorMessage(i, MessageUtil.getMessage(MessageConstant.Language.NOT_FOUND));
                continue;
            }
            JobLevel level;
            try {
                level = JobLevel.valueOf(levelString);
            } catch (IllegalArgumentException e) {
                parseResult.addErrorMessage(i, MessageUtil.getMessage(MessageConstant.JobLevel.NOT_FOUND));
                continue;
            }
            JobType jobType;
            try {
                jobType = JobType.valueOf(jobTypeString);
            } catch (IllegalArgumentException e) {
                parseResult.addErrorMessage(i, MessageUtil.getMessage(MessageConstant.JobType.NOT_FOUND));
                continue;
            }
            List<Integer> subCategoryIds = new ArrayList<>();
            try {
                Arrays.stream(subCategoryIdsString.split(FileConstant.CSV_CONSTANT.MULTIPLE_VALUE_DELIMITER)).forEach(valueString -> {
                    subCategoryIds.add((int) Double.parseDouble(valueString));
                });
            } catch (NumberFormatException e) {
                parseResult.addErrorMessage(i, MessageUtil.getMessage(MessageConstant.SubCategory.NOT_FOUND));
                continue;
            }
            List<Integer> skillTagIds = new ArrayList<>();
            try {
                Arrays.stream(skillTagIdsString.split(FileConstant.CSV_CONSTANT.MULTIPLE_VALUE_DELIMITER)).forEach(valueString -> {
                    skillTagIds.add((int) Double.parseDouble(valueString));
                });
            } catch (NumberFormatException e) {
                parseResult.addErrorMessage(i, MessageUtil.getMessage(MessageConstant.SkillTag.NOT_FOUND));
                continue;
            }
            CreateJobPositionRequest request = new CreateJobPositionRequest(title, contactPersonName, contactEmail, preferredLanguage
                    , level, jobType, locationId, subCategoryIds, skillTagIds, description, requirements);
            Errors errors = new BindException(request, CreateJobPositionRequest.class.getSimpleName());
            validator.validate(request, errors);
            if (errors.hasErrors()) {
                StringBuilder message = new StringBuilder("");
                for (ObjectError error : errors.getAllErrors()) {
                    message.append(((FieldError) error).getField());
                    message.append(" ");
                    message.append(error.getDefaultMessage());
                    message.append(".");
                }
                parseResult.addErrorMessage(i, message.toString());
                continue;
            }
            CompanyDTO companyDTO = CompanyDTO.builder().id(companyId).build();
            JobPositionDTO jobPositionDTO = mapper.toDTO(request);
            jobPositionDTO.setCompanyDTO(companyDTO);
            parseResult.addToResult(jobPositionDTO);

        }
        List<JobPositionDTO> insertResult = new ArrayList<>();
        if (!parseResult.isHasError()) {
            for (int i = 0; i < parseResult.getResult().size(); i++) {
                JobPositionDTO jobPositionDTO = parseResult.getResult().get(i);
                try {
                    jobPositionDTO = this.createNewJobPosition(jobPositionDTO);
                    insertResult.add(jobPositionDTO);
                } catch (IllegalArgumentException e) {
                    //+1 because row start at 1
                    parseResult.addErrorMessage(i + 1, e.getMessage());
                }
            }
            parseResult.setResult(insertResult);
        }
        return parseResult;

    }

    @SneakyThrows
    private ParseFileResult<JobPositionDTO> parseExcelFile(MultipartFile file, String companyId) {
        ParseFileResult<JobPositionDTO> parseResult;
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        if (workbook.getNumberOfSheets() != 1) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.File.XSL_NO_SHEET));
        }
        Sheet sheet = workbook.getSheetAt(0);
        List<List<String>> data = xslsFileService.readXSLSheet(sheet, JobPositionConstant.XLSXFormat.COLUMN_NUM);
        parseResult = createNewJobPositionFromListString(data, companyId);

        if (parseResult.isHasError()) {
            String url = xslsFileService.uploadErrorXSLFile(workbook, parseResult.getErrors(), file.getOriginalFilename(), JobPositionConstant.XLSXFormat.ERROR_INDEX);
            parseResult.setErrorFileUrl(url);
        }

        return parseResult;
    }

    @SneakyThrows
    private ParseFileResult<JobPositionDTO> parseCsvFile(MultipartFile file, String companyId) {
        ParseFileResult<JobPositionDTO> parseResult;
        List<List<String>> data = xslsFileService.readCSVFile(file.getInputStream());
        parseResult = createNewJobPositionFromListString(data, companyId);
        if (parseResult.isHasError()) {
            String url = xslsFileService.uploadErrorCSVFile(data, parseResult.getErrors(), file.getOriginalFilename());
            parseResult.setErrorFileUrl(url);
        }
        return parseResult;
    }

    @SneakyThrows
    @Override
    @Transactional
    public ParseFileResult<JobPositionDTO> createNewJobPositionsFromFile(MultipartFile file, String companyId) {
        //check for invalid type
        List<String> allowTypes = Arrays.asList(FileConstant.CSV_CONSTANT.TYPE, FileConstant.XLS_CONSTANT.TYPE, FileConstant.XLSX_CONSTANT.TYPE);
        String fileType = file.getContentType();
        if (!allowTypes.contains(fileType)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.File.NOT_ALLOWED));
        }
        ParseFileResult<JobPositionDTO> parseResult;
        if (Objects.equals(fileType, FileConstant.XLSX_CONSTANT.TYPE) || Objects.equals(fileType, FileConstant.XLS_CONSTANT.TYPE)) {
            parseResult = parseExcelFile(file, companyId);
            return parseResult;
        }
        parseResult = parseCsvFile(file, companyId);
        return parseResult;
    }

    @Override
    public Optional<JobPositionDTO> getByIdAndCompanyId(String id, String companyId) {
        return jobPositionRepository.findByIdAndCompanyId(id, companyId).map(mapper::toDTO);
    }


}
