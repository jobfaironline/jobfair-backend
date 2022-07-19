package org.capstone.job_fair.services.impl.company.job.question;

import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.FileConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.constants.QuestionConstant;
import org.capstone.job_fair.controllers.payload.requests.company.CreateQuestionCSVRequest;
import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.models.dtos.company.job.questions.ChoicesDTO;
import org.capstone.job_fair.models.dtos.company.job.questions.QuestionsDTO;
import org.capstone.job_fair.models.dtos.util.ParseFileResult;
import org.capstone.job_fair.models.entities.company.job.JobPositionEntity;
import org.capstone.job_fair.models.entities.company.job.questions.QuestionsEntity;
import org.capstone.job_fair.models.statuses.QuestionStatus;
import org.capstone.job_fair.repositories.company.job.JobPositionRepository;
import org.capstone.job_fair.repositories.company.job.questions.QuestionsRepository;
import org.capstone.job_fair.services.interfaces.company.job.question.QuestionsService;
import org.capstone.job_fair.services.interfaces.util.XSLSFileService;
import org.capstone.job_fair.services.mappers.company.job.question.QuestionsMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionsServiceImpl implements QuestionsService {

    @Autowired
    private JobPositionRepository jobPositionRepository;

    @Autowired
    private QuestionsRepository questionsRepository;

    @Autowired
    private QuestionsMapper questionsMapper;

    @Autowired
    private XSLSFileService xslsFileService;

    @Autowired
    private Validator validator;

    private QuestionsDTO createQuestion(QuestionsDTO questionsDTO) {
        questionsDTO.setCreateDate(new Date().getTime());
        questionsDTO.setStatus(QuestionStatus.ACTIVE);
        QuestionsEntity questionsEntity = questionsMapper.toEntity(questionsDTO);
        questionsEntity = questionsRepository.save(questionsEntity);
        return questionsMapper.toDTO(questionsEntity);
    }

    @Override
    @Transactional
    public QuestionsDTO createQuestion(QuestionsDTO questionsDTO, String id, String companyId) {
        Optional<JobPositionEntity> jobPositionEntityOptional = jobPositionRepository.findByIdAndCompanyId(questionsDTO.getJobPosition().getId(), companyId);
        if (!jobPositionEntityOptional.isPresent())
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.JOB_POSITION_NOT_FOUND));
        return createQuestion(questionsDTO);
    }

    @Override
    @Transactional
    public Optional<QuestionsDTO> getQuestionById(String id, String companyId) {
        Optional<QuestionsEntity> questionsEntityOptional = questionsRepository.findByIdAndJobPositionCompanyId(id, companyId);
        return questionsEntityOptional.map(entity -> questionsMapper.toDTO(entity));
    }

    @Override
    public Page<QuestionsDTO> getQuestionsByCriteria(String companyId, String content, long fromDate, long toDate, QuestionStatus status, int pageSize, int offset, String sortBy, Sort.Direction direction) {
        long date = new Date().getTime();
        if (fromDate == 0) fromDate = date - QuestionConstant.ONE_YEAR;
        if (toDate == 0) toDate = date + QuestionConstant.ONE_YEAR;
        if (offset < DataConstraint.Paging.OFFSET_MIN || pageSize < DataConstraint.Paging.PAGE_SIZE_MIN)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.INVALID_PAGE_NUMBER));
        if (fromDate > toDate)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Question.INVALID_DATE_RANGE));
        Page<QuestionsEntity> questionsEntityPage = questionsRepository.findAllByContentContainsAndCreateDateBetweenAndStatusAndJobPositionCompanyId(content, fromDate, toDate, status, companyId, PageRequest.of(offset, pageSize).withSort(Sort.by(direction, sortBy)));
        return questionsEntityPage.map(entity -> questionsMapper.toDTO(entity));
    }

    @Override
    @Transactional
    public QuestionsDTO deleteQuestion(String questionId, String companyId) {
        Optional<QuestionsEntity> questionsEntityOptional = questionsRepository.findByIdAndJobPositionCompanyId(questionId, companyId);
        if (!questionsEntityOptional.isPresent())
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Question.NOT_FOUND));
        QuestionsEntity questionsEntity = questionsEntityOptional.get();
        questionsRepository.save(questionsEntity);
        return questionsMapper.toDTO(questionsEntity);
    }

    @Override
    @Transactional
    public QuestionsDTO updateQuestion(QuestionsDTO dto, String companyId) {
        Optional<JobPositionEntity> jobPositionEntityOptional = jobPositionRepository.findByIdAndCompanyId(dto.getJobPosition().getId(), companyId);
        if (!jobPositionEntityOptional.isPresent())
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.JOB_POSITION_NOT_FOUND));
        Optional<QuestionsEntity> questionsEntityOptional = questionsRepository.findByIdAndJobPositionCompanyId(dto.getId(), companyId);
        if (!questionsEntityOptional.isPresent())
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Question.NOT_FOUND));
        dto.setUpdateDate(new Date().getTime());
        QuestionsEntity questionsEntity = questionsEntityOptional.get();
        questionsMapper.updateQuestion(dto, questionsEntity);
        questionsEntity = questionsRepository.save(questionsEntity);
        return questionsMapper.toDTO(questionsEntity);
    }

    @Override
    public Page<QuestionsDTO> getQuestionByJobPosition(String companyId, String jobPositionId, String searchContent, QuestionStatus status, int offset, int pageSize, String sortBy, Sort.Direction direction) {
        if (offset < DataConstraint.Paging.OFFSET_MIN || pageSize < DataConstraint.Paging.PAGE_SIZE_MIN)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.INVALID_PAGE_NUMBER));
        Page<QuestionsEntity> questionsEntityPage = questionsRepository.findAllByContentContainsAndJobPositionIdAndJobPositionCompanyIdAndStatus(searchContent, jobPositionId, companyId, status, PageRequest.of(offset, pageSize).withSort(Sort.by(direction, sortBy)));
        return questionsEntityPage.map(entity -> questionsMapper.toDTO(entity));
    }


    private ParseFileResult<QuestionsDTO> createNewQuestionsFromListString(List<List<String>> data, String jobPositionId) {
        ParseFileResult<QuestionsDTO> parseResult = new ParseFileResult();

        int rowNum = data.size();
        QuestionsDTO questionsDTO = null;
        for (int i = 1; i < rowNum; i++) {
            List<String> rowData = data.get(i);
            String content = rowData.get(QuestionConstant.XLSXFormat.CONTENT_INDEX);
            Boolean isQuestion = null;
            try {
                isQuestion = Double.parseDouble(rowData.get(QuestionConstant.XLSXFormat.IS_QUESTION_INDEX)) != 0.0;
            } catch (NumberFormatException e) {
            }
            Boolean isCorrect = null;
            try {
                isCorrect = Double.parseDouble(rowData.get(QuestionConstant.XLSXFormat.IS_CORRECT_INDEX)) == 1.0;
            } catch (NumberFormatException e) {
            }

            CreateQuestionCSVRequest csvRequest = new CreateQuestionCSVRequest(content, isQuestion, isCorrect);
            Errors errors = new BindException(csvRequest, CreateQuestionCSVRequest.class.getSimpleName());
            validator.validate(csvRequest, errors);
            if (errors.hasErrors()) {
                StringBuilder message = new StringBuilder("");
                for (FieldError error : errors.getFieldErrors()) {
                    message.append(error.getField());
                    message.append(" ");
                    message.append(error.getDefaultMessage());
                    message.append(".");
                }
                parseResult.addErrorMessage(i, message.toString());
                continue;
            }
            //if first line is not the question
            if (questionsDTO == null && !isQuestion) {
                parseResult.addErrorMessage(i, MessageUtil.getMessage(MessageConstant.Question.CSV_WRONG_FORMAT));
                break;
            }
            if (isQuestion) {
                if (questionsDTO != null) {
                    //before create new question, check old question has at least 1 correct answer
                    long correctChoiceCount = questionsDTO.getChoicesList().stream().filter(ChoicesDTO::getIsCorrect).count();
                    if (correctChoiceCount == 0) {
                        parseResult.addErrorMessage(i, MessageUtil.getMessage(MessageConstant.Question.LACK_CORRECT_ANSWER));
                        continue;
                    }
                }

                JobPositionDTO jobPositionDTO = JobPositionDTO.builder().id(jobPositionId).build();
                questionsDTO = QuestionsDTO.builder().content(content).jobPosition(jobPositionDTO).build();
                questionsDTO.setChoicesList(new ArrayList<>());
                parseResult.addToResult(questionsDTO);
            } else {
                ChoicesDTO choicesDTO = ChoicesDTO.builder().content(content).isCorrect(isCorrect).build();
                questionsDTO.getChoicesList().add(choicesDTO);
            }


        }
        if (!parseResult.isHasError()) {
            List<QuestionsDTO> insertResult = parseResult.getResult().parallelStream().map(this::createQuestion).collect(Collectors.toList());
            parseResult.setResult(insertResult);
        }
        return parseResult;
    }

    @SneakyThrows
    private ParseFileResult<QuestionsDTO> parseExcelFile(MultipartFile file, String jobPositionId) {
        ParseFileResult<QuestionsDTO> parseResult;
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        if (workbook.getNumberOfSheets() != 1) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.File.XSL_NO_SHEET));
        }
        Sheet sheet = workbook.getSheetAt(0);
        List<List<String>> data = xslsFileService.readXSLSheet(sheet, QuestionConstant.XLSXFormat.COLUMN_NUM);
        parseResult = createNewQuestionsFromListString(data, jobPositionId);

        if (parseResult.isHasError()) {
            String url = xslsFileService.uploadErrorXSLFile(workbook, parseResult.getErrors(), file.getOriginalFilename(), QuestionConstant.XLSXFormat.ERROR_INDEX);
            parseResult.setErrorFileUrl(url);
        }

        return parseResult;
    }

    @SneakyThrows
    private ParseFileResult<QuestionsDTO> parseCsvFile(MultipartFile file, String jobPositionId) {
        ParseFileResult<QuestionsDTO> parseResult;
        List<List<String>> data = xslsFileService.readCSVFile(file.getInputStream());
        parseResult = createNewQuestionsFromListString(data, jobPositionId);
        if (parseResult.isHasError()) {
            String url = xslsFileService.uploadErrorCSVFile(data, parseResult.getErrors(), file.getOriginalFilename());
            parseResult.setErrorFileUrl(url);
        }
        return parseResult;
    }


    @Override
    @Transactional
    @SneakyThrows
    public ParseFileResult<QuestionsDTO> createNewQuestionsFromFile(MultipartFile file, String jobPositionId) {
        //check for invalid type
        List<String> allowTypes = Arrays.asList(FileConstant.CSV_CONSTANT.TYPE, FileConstant.XLS_CONSTANT.TYPE, FileConstant.XLSX_CONSTANT.TYPE);
        String fileType = file.getContentType();
        if (!allowTypes.contains(fileType)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.File.NOT_ALLOWED));
        }
        ParseFileResult<QuestionsDTO> parseResult;
        if (Objects.equals(fileType, FileConstant.XLSX_CONSTANT.TYPE) || Objects.equals(fileType, FileConstant.XLS_CONSTANT.TYPE)) {
            parseResult = parseExcelFile(file, jobPositionId);
            return parseResult;
        }
        parseResult = parseCsvFile(file, jobPositionId);
        return parseResult;
    }


}
