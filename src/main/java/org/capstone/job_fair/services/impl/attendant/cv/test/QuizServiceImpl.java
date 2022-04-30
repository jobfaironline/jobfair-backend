package org.capstone.job_fair.services.impl.attendant.cv.test;

import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.attendant.cv.test.QuizChoiceDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.test.QuizDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.test.QuizQuestionDTO;
import org.capstone.job_fair.models.dtos.company.job.questions.ChoicesDTO;
import org.capstone.job_fair.models.dtos.company.job.questions.QuestionsDTO;
import org.capstone.job_fair.models.entities.attendant.cv.ApplicationEntity;
import org.capstone.job_fair.models.entities.attendant.cv.test.QuizChoiceEntity;
import org.capstone.job_fair.models.entities.attendant.cv.test.QuizEntity;
import org.capstone.job_fair.models.entities.attendant.cv.test.QuizQuestionEntity;
import org.capstone.job_fair.models.entities.company.job.questions.QuestionsEntity;
import org.capstone.job_fair.models.enums.TestStatus;
import org.capstone.job_fair.repositories.attendant.cv.ApplicationRepository;
import org.capstone.job_fair.repositories.attendant.cv.test.QuizChoiceRepository;
import org.capstone.job_fair.repositories.attendant.cv.test.QuizQuestionRepository;
import org.capstone.job_fair.repositories.attendant.cv.test.QuizRepository;
import org.capstone.job_fair.repositories.company.job.questions.QuestionsRepository;
import org.capstone.job_fair.services.interfaces.attendant.cv.test.QuizService;
import org.capstone.job_fair.services.mappers.attendant.cv.test.QuizChoiceMapper;
import org.capstone.job_fair.services.mappers.attendant.cv.test.QuizMapper;
import org.capstone.job_fair.services.mappers.attendant.cv.test.QuizQuestionMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private QuizMapper quizMapper;

    @Autowired
    private QuizQuestionMapper quizQuestionMapper;

    @Autowired
    private QuestionsRepository questionsRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private QuizChoiceMapper quizChoiceMapper;


    @Override
    @Transactional
    public QuizDTO createQuiz(String applicationId, String jobPositionId, int numberOfQuestion) {
        //Create new entity
        QuizEntity entity = new QuizEntity();
        entity = quizRepository.save(entity);
        List<QuestionsEntity> questionsEntities = questionsRepository.getRandomQuestion(jobPositionId, numberOfQuestion);
        List<QuizQuestionEntity> quizQuestionEntities = questionsEntities.stream().map(question -> quizQuestionMapper.toEntity(question)).collect(Collectors.toList());
        entity.setQuestionList(quizQuestionEntities);
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setId(applicationId);
        entity.setApplication(applicationEntity);
        entity = quizRepository.save(entity);
        return quizMapper.toDTO(entity);
    }

    @Override
    @Transactional
    public Optional<QuizDTO> getQuiz(String applicationId) {
        Optional<QuizEntity> entityOptional = quizRepository.findByApplicationId(applicationId);
        if (!entityOptional.isPresent()) {
            return Optional.empty();
        }
        ApplicationEntity applicationEntity = applicationRepository.getById(applicationId);
        QuizEntity quizEntity = entityOptional.get();
        //Attendant can only take test when test status is NOT_TAKEN
        //OR
        //Status is DOING then
        //Current time is between start time and end time
        if (applicationEntity.getTestStatus().equals(TestStatus.DOING) || applicationEntity.getTestStatus().equals(TestStatus.NOT_TAKEN)) {
            if (applicationEntity.getTestStatus().equals(TestStatus.DOING)) {
                Long endTime = quizEntity.getEndTime();
                Long currentTime = new Date().getTime();
                if (currentTime > endTime) {
                    throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Quiz.TIME_UP));
                }
            }
        } else {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Quiz.TEST_ALREADY_TAKEN));
        }
        //If status is NOT_TAKEN then set START TIME, END TIME and change status to doing
        if (applicationEntity.getTestStatus().equals(TestStatus.NOT_TAKEN)) {
            applicationEntity.setTestStatus(TestStatus.DOING);
            quizEntity.setBeginTime(new Date().getTime());
            quizEntity.setEndTime(quizEntity.getBeginTime() + applicationEntity.getBoothJobPosition().getTestTimeLength());
            quizEntity = quizRepository.save(quizEntity);
            applicationRepository.save(applicationEntity);
        }
        return Optional.of(quizMapper.toDTO(quizEntity));
    }

    private QuizEntity validatequiz(String applicationId){
        Optional<ApplicationEntity> applicationEntityOptional = applicationRepository.findById(applicationId);
        //Check if application exist
        if (!applicationEntityOptional.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.CV_NOT_FOUND));
        }
        ApplicationEntity applicationEntity = applicationEntityOptional.get();
        //If application existed then check if job position has test
        if (!applicationEntity.getBoothJobPosition().getIsHaveTest()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.CV_NOT_HAVE_TEST));
        }
        //If job position has test then check if test status is DOING
        if (!applicationEntity.getTestStatus().equals(TestStatus.DOING)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Quiz.TEST_ALREADY_TAKEN));
        }
        //Application has test then get quiz
        QuizEntity quizEntity = quizRepository.findByApplicationId(applicationId).get();
        //Validate time is not over
        Long endTime = quizEntity.getEndTime();
        Long currentTime = new Date().getTime();
        if (currentTime > endTime) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Quiz.TIME_UP));
        }
        return quizEntity;
    }

    @Override
    @Transactional
    public QuizDTO saveQuiz(String applicationId, List<QuizQuestionDTO> answers) {
        QuizEntity quizEntity = validatequiz(applicationId);
        List<QuizChoiceDTO> answerChoice = null;

        for (QuizQuestionDTO answer : answers) {
            answerChoice = answer.getChoiceList();
            for (QuizQuestionEntity question : quizEntity.getQuestionList()) {
                if (question.getId().equals(answer.getId())) {
                    for (QuizChoiceEntity choice : question.getChoiceList()) {
                        for (QuizChoiceDTO choiceDTO : answerChoice) {
                            if (choice.getId().equals(choiceDTO.getId())) {
                                choice.setIsSelected(choiceDTO.getIsSelected());
                            }
                        }
                    }
                }
            }
        }

        return quizMapper.toDTO(quizRepository.save(quizEntity));
    }

    @Override
    @Transactional
    public void submitQuiz(String applicationId) {
        QuizEntity quizEntity = validatequiz(applicationId);
        ApplicationEntity applicationEntity = applicationRepository.getById(applicationId);
        applicationEntity.setTestStatus(TestStatus.DONE);
        applicationRepository.save(applicationEntity);
        quizEntity.setIsSubmitted(true);
        quizRepository.save(quizEntity);
    }
}
