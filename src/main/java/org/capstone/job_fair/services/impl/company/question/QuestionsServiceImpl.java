package org.capstone.job_fair.services.impl.company.question;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.SneakyThrows;
import org.capstone.job_fair.constants.CSVConstant;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.constants.QuestionConstant;
import org.capstone.job_fair.controllers.payload.requests.company.CreateQuestionCSVRequest;
import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.models.dtos.company.job.questions.ChoicesDTO;
import org.capstone.job_fair.models.dtos.company.job.questions.QuestionsDTO;
import org.capstone.job_fair.models.entities.company.job.JobPositionEntity;
import org.capstone.job_fair.models.entities.company.job.questions.QuestionsEntity;
import org.capstone.job_fair.models.statuses.QuestionStatus;
import org.capstone.job_fair.repositories.company.job.JobPositionRepository;
import org.capstone.job_fair.repositories.company.job.questions.QuestionsRepository;
import org.capstone.job_fair.services.interfaces.company.question.QuestionsService;
import org.capstone.job_fair.services.mappers.company.question.QuestionsMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.InputStreamReader;
import java.io.Reader;
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
        questionsEntity.setStatus(QuestionStatus.INACTIVE);
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

    @Override
    @Transactional
    @SneakyThrows
    public List<QuestionsDTO> createNewQuestionsFromCSVFile(MultipartFile file, String jobPositionId) {
        List<QuestionsDTO> result = new ArrayList<>();
        if (!CSVConstant.TYPE.equals(file.getContentType())) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.CSV_FILE_ERROR));
        }
        //check existed job position
        Optional<JobPositionEntity> jobPositionOpt = jobPositionRepository.findById(jobPositionId);
        jobPositionOpt.orElseThrow(() -> new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.JOB_POSITION_NOT_FOUND)));

        Reader reader = new InputStreamReader(file.getInputStream());
        CsvToBean<CreateQuestionCSVRequest> csvToBean = new CsvToBeanBuilder(reader)
                .withType(CreateQuestionCSVRequest.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        Iterator<CreateQuestionCSVRequest> questionCSVs = csvToBean.iterator();
        QuestionsDTO questionsDTO = null;
        int numberOfCreatedJob = 0;
        while (questionCSVs.hasNext()) {
            numberOfCreatedJob++;
            CreateQuestionCSVRequest csvRequest = questionCSVs.next();
            Errors errors = new BindException(csvRequest, CreateQuestionCSVRequest.class.getSimpleName());
            validator.validate(csvRequest, errors);
            if (errors.hasErrors()) {
                String errorMessage = String.format(MessageUtil.getMessage(MessageConstant.Job.CSV_LINE_ERROR), numberOfCreatedJob);
                throw new IllegalArgumentException(errorMessage);
            }

            //if first line is not the question
            if (questionsDTO == null && !csvRequest.getIsQuestion()) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Question.CSV_WRONG_FORMAT));
            }

            if (csvRequest.getIsQuestion()){
                if (questionsDTO != null){
                    //before create new question, check old question has at least 1 correct answer
                    long correctChoiceCount = questionsDTO.getChoicesList().stream().filter(ChoicesDTO::getIsCorrect).count();
                    if (correctChoiceCount == 0) {
                        throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Question.LACK_CORRECT_ANSWER));
                    }
                }

                JobPositionDTO jobPositionDTO = JobPositionDTO.builder().id(jobPositionId).build();
                questionsDTO = QuestionsDTO.builder().content(csvRequest.getContent()).jobPosition(jobPositionDTO).build();
                questionsDTO.setChoicesList(new ArrayList<>());
                result.add(questionsDTO);
            } else {
                ChoicesDTO choicesDTO = ChoicesDTO.builder().content(csvRequest.getContent()).isCorrect(csvRequest.getIsCorrect()).build();
                questionsDTO.getChoicesList().add(choicesDTO);
            }
        }
        result = result.stream().map(this::createQuestion).collect(Collectors.toList());
        return result;
    }


}
