package org.capstone.job_fair.services.impl.job_fair.booth;

import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.capstone.job_fair.constants.*;
import org.capstone.job_fair.models.dtos.job_fair.booth.AssignmentDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.BoothJobPositionDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.JobFairBoothDTO;
import org.capstone.job_fair.models.dtos.util.ParseFileResult;
import org.capstone.job_fair.models.entities.company.job.JobPositionEntity;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.JobFairBoothEntity;
import org.capstone.job_fair.models.statuses.JobFairPlanStatus;
import org.capstone.job_fair.repositories.company.job.JobPositionRepository;
import org.capstone.job_fair.repositories.job_fair.JobFairRepository;
import org.capstone.job_fair.repositories.job_fair.job_fair_booth.JobFairBoothRepository;
import org.capstone.job_fair.services.interfaces.job_fair.booth.JobFairBoothService;
import org.capstone.job_fair.services.interfaces.util.XSLSFileService;
import org.capstone.job_fair.services.mappers.job_fair.booth.JobFairBoothMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Clock;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class JobFairBoothServiceImpl implements JobFairBoothService {
    @Autowired
    private JobFairBoothRepository jobFairBoothRepository;

    @Autowired
    private JobFairRepository jobFairRepository;

    @Autowired
    private JobFairBoothMapper jobFairBoothMapper;

    @Autowired
    private JobPositionRepository jobPositionRepository;

    @Autowired
    private Clock clock;

    @Autowired
    private XSLSFileService xslsFileService;

    @Override
    public Optional<JobFairBoothDTO> getCompanyBoothByJobFairIdAndBoothId(String jobFairId, String boothId) {
        return jobFairBoothRepository.findByJobFairIdAndBoothId(jobFairId, boothId).map(jobFairBoothMapper::toDTO);
    }

    @Override
    public List<JobFairBoothDTO> getCompanyBoothByJobFairId(String jobFairId) {
        Optional<JobFairEntity> jobFairOpt = jobFairRepository.findById(jobFairId);
        if (!jobFairOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.JOB_FAIR_NOT_FOUND));
        }
        return jobFairBoothRepository.findByJobFairId(jobFairId)
                .stream()
                .map(jobFairBoothMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<JobFairBoothDTO> getById(String boothId) {
        return jobFairBoothRepository.findById(boothId).map(jobFairBoothMapper::toDTO);
    }

    @Override
    public Integer getBoothCountByJobFair(String jobFairId) {
        return jobFairBoothRepository.countByJobFairId(jobFairId);
    }


    private void validateUniqueJobPosition(List<BoothJobPositionDTO> jobPositions) {
        jobPositions.sort(Comparator.comparing(BoothJobPositionDTO::getOriginJobPosition));
        for (int i = 0; i <= jobPositions.size() - 2; i++) {
            BoothJobPositionDTO currentDTO = jobPositions.get(i);
            BoothJobPositionDTO nextDTO = jobPositions.get(i + 1);
            if (currentDTO.getOriginJobPosition().equals(nextDTO.getOriginJobPosition())) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFairBooth.UNIQUE_JOB_POSITION_ERROR));
            }
        }
    }

    @Override
    @Transactional
    public JobFairBoothDTO updateJobFairBooth(JobFairBoothDTO jobFairBooth, String companyId) {
        //check for valid job fair booth
        Optional<JobFairBoothEntity> jobFairBoothOpt = jobFairBoothRepository.findByIdAndJobFairCompanyId(jobFairBooth.getId(), companyId);
        if (!jobFairBoothOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFairBooth.NOT_FOUND));
        }
        JobFairBoothEntity jobFairBoothEntity = jobFairBoothOpt.get();
        //check unique job position
        validateUniqueJobPosition(jobFairBooth.getBoothJobPositions());
        //check job fair status
        JobFairEntity jobFairEntity = jobFairBoothEntity.getJobFair();
        if (jobFairEntity.getStatus() == JobFairPlanStatus.DRAFT) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.NOT_EDITABLE));
        }
        //check decorate time
        long now = clock.millis();
        if (now < jobFairEntity.getDecorateStartTime() || now > jobFairEntity.getDecorateEndTime()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.NOT_EDITABLE));
        }
        //check job position belongs in company
        jobFairBooth.getBoothJobPositions().forEach(boothJobPositionDTO -> {
            Optional<JobPositionEntity> jobPositionOpt = jobPositionRepository.findById(boothJobPositionDTO.getOriginJobPosition());
            if (!jobPositionOpt.isPresent()) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.JOB_POSITION_NOT_FOUND));
            }
            JobPositionEntity jobPosition = jobPositionOpt.get();
            if (!jobPosition.getCompany().getId().equals(companyId)) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.COMPANY_MISMATCH));
            }
        });


        jobFairBoothMapper.updateEntity(jobFairBooth, jobFairBoothEntity);
        jobFairBoothEntity.getBoothJobPositions().forEach(boothPosition -> {
            Optional<JobPositionEntity> jobPositionOpt = jobPositionRepository.findById(boothPosition.getOriginJobPosition());
            JobPositionEntity jobPositionEntity = jobPositionOpt.get();
            boothPosition.setTitle(jobPositionEntity.getTitle());
            boothPosition.setContactEmail(jobPositionEntity.getContactEmail());
            boothPosition.setContactPersonName(jobPositionEntity.getContactPersonName());
            boothPosition.setLanguage(jobPositionEntity.getLanguage());
            boothPosition.setJobLevel(jobPositionEntity.getJobLevel());
            boothPosition.setJobTypeEntity(jobPositionEntity.getJobTypeEntity());
            boothPosition.setCategories(new HashSet<>(jobPositionEntity.getCategories()));
            boothPosition.setSkillTagEntities(new HashSet<>(jobPositionEntity.getSkillTagEntities()));
            boothPosition.setDescription(jobPositionEntity.getDescription());
            boothPosition.setRequirements(jobPositionEntity.getRequirements());
            boothPosition.setRequirementKeyWord(jobPositionEntity.getRequirementKeyWord());
            boothPosition.setDescriptionKeyWord(jobPositionEntity.getDescriptionKeyWord());
        });
        jobFairBoothRepository.save(jobFairBoothEntity);
        return jobFairBooth;
    }


    private ParseFileResult<JobFairBoothDTO> assignJobPositionFromListString(List<List<String>> data, String jobFairBoothId, String companyId){
        ParseFileResult<JobFairBoothDTO> parseResult = new ParseFileResult<>();
        int rowNum = data.size();

        List<BoothJobPositionDTO> jobPositionDTOS = new ArrayList<>();

        for (int i = 1; i < rowNum; i++) {
            List<String> rowData = data.get(i);
            String jobName = rowData.get(JobFairBoothConstant.XLSXFormat.JOB_NAME_INDEX);
            String note = rowData.get(JobFairBoothConstant.XLSXFormat.NOTE_INDEX);
            String numOfPositionStr = rowData.get(JobFairBoothConstant.XLSXFormat.NUM_OF_POSITION_INDEX);
            String minSalaryStr = rowData.get(JobFairBoothConstant.XLSXFormat.MIN_SALARY_INDEX);
            String maxSalaryStr = rowData.get(JobFairBoothConstant.XLSXFormat.MAX_SALARY_INDEX);
            String haveTestStr = rowData.get(JobFairBoothConstant.XLSXFormat.HAVE_TEST_INDEX);
            String testDurationStr = rowData.get(JobFairBoothConstant.XLSXFormat.TEST_DURATION_INDEX);
            String passMarkStr = rowData.get(JobFairBoothConstant.XLSXFormat.PASS_MARK_INDEX);
            String numOfQuestionStr = rowData.get(JobFairBoothConstant.XLSXFormat.NUM_OF_QUESTION_INDEX);

            int numOfPosition =  0;
            try {
                numOfPosition = (int) Double.parseDouble(numOfPositionStr);
                if (numOfPosition < DataConstraint.JobPosition.EMPLOYEE_MIN || numOfPosition > DataConstraint.JobPosition.EMPLOYEE_MAX) throw new NumberFormatException();
            } catch (NumberFormatException e){
                parseResult.addErrorMessage(i, "Invalid number of position");
            }

            double minSalary = 0.0;
            try {
                minSalary = Double.parseDouble(minSalaryStr);
                if (minSalary < DataConstraint.JobPosition.SALARY_MIN || minSalary > DataConstraint.JobPosition.SALARY_MAX) throw  new NumberFormatException();
            } catch (NumberFormatException e){
                parseResult.addErrorMessage(i, "Invalid max salary");
            }

            double maxSalary = 0.0;
            try {
                maxSalary = Double.parseDouble(maxSalaryStr);
                if (maxSalary < DataConstraint.JobPosition.SALARY_MIN || maxSalary > DataConstraint.JobPosition.SALARY_MAX) throw  new NumberFormatException();
            } catch (NumberFormatException e){
                parseResult.addErrorMessage(i, "Invalid max salary");
            }

            if (maxSalary < minSalary){
                parseResult.addErrorMessage(i, "Max salary must be bigger than min salary");
            }

            boolean haveTest = false;
            try {
                haveTest = Double.parseDouble(haveTestStr) != 0.0;
            } catch (NumberFormatException ignore) {
                parseResult.addErrorMessage(i, "Invalid hasTest value");
            }

            if (parseResult.isHasError()) continue;
            BoothJobPositionDTO jobPositionDTO = new BoothJobPositionDTO();
            jobPositionDTO.setNote(note);
            jobPositionDTO.setNumOfPosition(numOfPosition);
            jobPositionDTO.setMaxSalary(maxSalary);
            jobPositionDTO.setMinSalary(minSalary);
            jobPositionDTO.setIsHaveTest(haveTest);

            if (haveTest){
                int testDuration = 0;
                try {
                    testDuration = (int) Double.parseDouble(testDurationStr);
                    if (testDuration < DataConstraint.JobPosition.TEST_LENGTH_MIN || testDuration > DataConstraint.JobPosition.TEST_LENGTH_MAX) throw new NumberFormatException();
                } catch (NumberFormatException e){
                    parseResult.addErrorMessage(i, "Invalid test duration");
                }

                double passMark = 0.0;
                try {
                    passMark = Double.parseDouble(passMarkStr);
                    if (passMark < DataConstraint.JobPosition.PASS_MARK_MIN || passMark > DataConstraint.JobPosition.PASS_MARK_MAX) throw  new NumberFormatException();
                } catch (NumberFormatException e){
                    parseResult.addErrorMessage(i, "Invalid pass mark");
                }

                int numOfQuestion = 0;
                try {
                    numOfQuestion =  (int) Double.parseDouble(numOfQuestionStr);
                    if (numOfQuestion < DataConstraint.JobPosition.NUM_OF_QUESTION_MIN || numOfQuestion > DataConstraint.JobPosition.NUM_OF_QUESTION_MAX) throw new NumberFormatException();
                } catch (NumberFormatException e){
                    parseResult.addErrorMessage(i, "Invalid number of question");
                }

                jobPositionDTO.setTestTimeLength(testDuration);
                jobPositionDTO.setPassMark(passMark);
                jobPositionDTO.setNumOfQuestion(numOfQuestion);
            }
            if (parseResult.isHasError()) continue;

            Optional<JobPositionEntity> jobPositionOpt = jobPositionRepository.findFirstByTitleLikeAndCompanyId(jobName, companyId);
            if (!jobPositionOpt.isPresent()){
                parseResult.addErrorMessage(i, MessageUtil.getMessage(MessageConstant.Job.JOB_POSITION_NOT_FOUND));
                continue;
            }
            JobPositionEntity originJobPosition = jobPositionOpt.get();
            jobPositionDTO.setOriginJobPosition(originJobPosition.getId());
            jobPositionDTOS.add(jobPositionDTO);

        }

        Optional<JobFairBoothEntity> jobFairBoothOpt = jobFairBoothRepository.findById(jobFairBoothId);
        if (!jobFairBoothOpt.isPresent()){
            parseResult.addErrorMessage(rowNum, MessageUtil.getMessage(MessageConstant.Job.JOB_POSITION_NOT_FOUND));
            return parseResult;
        }

        JobFairBoothDTO jobFairBoothDTO = jobFairBoothMapper.toDTO(jobFairBoothOpt.get());

        jobFairBoothDTO.setBoothJobPositions(jobPositionDTOS);

        jobFairBoothDTO = updateJobFairBooth(jobFairBoothDTO, companyId);
        parseResult.addToResult(jobFairBoothDTO);
        return parseResult;
    }

    @Override
    @SneakyThrows
    @Transactional
    public ParseFileResult<JobFairBoothDTO> assignJobPositionToJobFairBoothByFile(String jobFairBoothId, String companyId, MultipartFile file) {
        //check for valid job fair booth
        Optional<JobFairBoothEntity> jobFairBoothOpt = jobFairBoothRepository.findByIdAndJobFairCompanyId(jobFairBoothId, companyId);
        if (!jobFairBoothOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFairBooth.NOT_FOUND));
        }
        //check for invalid type
        List<String> allowTypes = Arrays.asList(FileConstant.CSV_CONSTANT.TYPE, FileConstant.XLS_CONSTANT.TYPE, FileConstant.XLSX_CONSTANT.TYPE);
        String fileType = file.getContentType();
        if (!allowTypes.contains(fileType)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.File.NOT_ALLOWED));
        }
        ParseFileResult<JobFairBoothDTO> parseResult;
        List<List<String>> data = null;

        //Excel file
        if (Objects.equals(fileType, FileConstant.XLSX_CONSTANT.TYPE) || Objects.equals(fileType, FileConstant.XLS_CONSTANT.TYPE)) {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            if (workbook.getNumberOfSheets() != 1) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.File.XSL_NO_SHEET));
            }
            Sheet sheet = workbook.getSheetAt(0);
            data = xslsFileService.readXSLSheet(sheet, JobFairBoothConstant.XLSXFormat.COLUMN_NUM);
            parseResult = assignJobPositionFromListString(data, jobFairBoothId, companyId);

            if (parseResult.isHasError()) {
                String url = xslsFileService.uploadErrorXSLFile(workbook, parseResult.getErrors(), file.getOriginalFilename(), AssignmentConstant.XLSXFormat.ERROR_INDEX);
                parseResult.setErrorFileUrl(url);
            }
            return parseResult;
        }
        //CSV file
        data = xslsFileService.readCSVFile(file.getInputStream());
        parseResult = assignJobPositionFromListString(data, jobFairBoothId, companyId);
        return parseResult;

    }
}
