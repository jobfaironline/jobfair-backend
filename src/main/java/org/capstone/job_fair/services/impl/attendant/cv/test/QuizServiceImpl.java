package org.capstone.job_fair.services.impl.attendant.cv.test;

import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.constants.QuizConstant;
import org.capstone.job_fair.models.dtos.attendant.cv.test.QuizChoiceDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.test.QuizDTO;
import org.capstone.job_fair.models.entities.attendant.application.ApplicationEntity;
import org.capstone.job_fair.models.entities.attendant.cv.test.QuizChoiceEntity;
import org.capstone.job_fair.models.entities.attendant.cv.test.QuizEntity;
import org.capstone.job_fair.models.entities.attendant.cv.test.QuizQuestionEntity;
import org.capstone.job_fair.models.entities.company.job.questions.QuestionsEntity;
import org.capstone.job_fair.models.enums.TestStatus;
import org.capstone.job_fair.repositories.attendant.cv.ApplicationRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
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
    public Optional<QuizDTO> getQuizById(String id, String applicationId, String userid) {
        Optional<QuizEntity> quizEntity = quizRepository.findByIdAndApplicationIdAndApplicationAttendantAccountId(id, applicationId, userid);
        return quizEntity.map(quizMapper::toDTO);
    }

    @Override
    @Transactional
    public QuizDTO createQuiz(String applicationId, String userId) {
        Optional<ApplicationEntity> applicationEntityOptional = applicationRepository.findByIdAndAttendantAccountId(applicationId, userId);
        if (!applicationEntityOptional.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.APPLICATION_NOT_FOUND));
        }
        //Check if this job position has any quiz
        ApplicationEntity applicationEntity = applicationEntityOptional.get();
        if (!applicationEntity.getBoothJobPosition().getIsHaveTest()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Quiz.JOB_POSITION_NOT_ALLOW_QUIZ));
        }
        //Check if there is any quiz is in progress at current time
        //By finding if there is any quiz that has start time < current time and end time > current time
        Long currentTime = new Date().getTime();
        Optional<QuizEntity> quizEntityOptional = quizRepository.getQuiz(currentTime, applicationId, userId);
        //if there is => Quiz is in progress
        if (quizEntityOptional.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Quiz.CURRENT_QUIZ_STILL_IN_PROGRESS));
        }
        //If not, generate quiz
        QuizEntity quizEntity = new QuizEntity();
        //Get number of question from job position
        int numberOfQuestion = applicationEntity.getBoothJobPosition().getNumOfQuestion();
        //Get duration from job position
        int duration = applicationEntity.getBoothJobPosition().getTestTimeLength();
        //Select random question from question bank
        String jobPositionId = applicationEntity.getBoothJobPosition().getOriginJobPosition();
        List<QuestionsEntity> questionsEntities = questionsRepository.getRandomQuestion(jobPositionId, numberOfQuestion);
        quizEntity.setQuestionList(questionsEntities.stream().map(entity -> quizQuestionMapper.toEntity(entity)).collect(Collectors.toList()));
        //Set start time to now
        quizEntity.setBeginTime(currentTime);
        //Set end time to now + duration
        quizEntity.setEndTime(currentTime + duration * 60 * 1000 + QuizConstant.BUFFER_TIME);
        quizEntity.setApplication(applicationEntity);
        //Change test status to in progress
        applicationEntity.setTestStatus(TestStatus.IN_PROGRESS);
        //Save quiz
        quizEntity = quizRepository.save(quizEntity);
        applicationRepository.save(applicationEntity);
        return quizMapper.toDTO(quizEntity);
    }

    private boolean checkQuizTime(QuizEntity quizEntity) {
        Long currentTime = new Date().getTime();
        return currentTime >= quizEntity.getBeginTime() && currentTime <= quizEntity.getEndTime();
    }

    private QuizEntity getQuizToSave(String quizId, String applicationId, String userId) {
        //Check if quiz is exist
        Optional<QuizEntity> quizEntityOptional = quizRepository.findQuizToSave(applicationId, quizId, userId);
        if (!quizEntityOptional.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Quiz.NOT_FOUND));
        }
        QuizEntity quizEntity = quizEntityOptional.get();
        //Check if quiz is opening
        if (quizEntity.getIsSubmitted()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Quiz.TEST_ALREADY_TAKEN));
        }
        return quizEntity;
    }

    @Override
    @Transactional
    public QuizDTO saveQuiz(String applicationId, String userId, String quizId, HashMap<String, Boolean> answers) {
        QuizEntity quizEntity = getQuizToSave(quizId, applicationId, userId);
        if (!checkQuizTime(quizEntity)) {
          return submitQuiz(applicationId, userId, quizId, quizEntity);
        }
        quizEntity.getQuestionList().forEach(entity -> {
            entity.getChoiceList().forEach(choice -> {
                if (answers.containsKey(choice.getId())) {
                    choice.setIsSelected(answers.get(choice.getId()));
                }
            });
        });
        return quizMapper.toDTO(quizRepository.save(quizEntity));
    }

    private Double evaluateQuiz(QuizEntity entity) {
        double mark = 0;
        double oneQuestionPoint = 10 / entity.getQuestionList().size();
        int numberOfAnswer;
        int numberOfWrongAnswer;
        int numberOfCorrectAnswer;
        for (QuizQuestionEntity question : entity.getQuestionList()) {
            numberOfAnswer = 0;
            numberOfWrongAnswer = 0;
            numberOfCorrectAnswer = 0;
            for (QuizChoiceEntity choice : question.getChoiceList()) {
                //If choice is correct then increase numberOfAnswer by 1
                if (choice.getIsCorrect()) {
                    ++numberOfAnswer;
                    if (choice.getIsSelected()) {
                        ++numberOfCorrectAnswer;
                    }
                }
                //If choice is selected, but it is not correct then increase numberOfWrongAnswer by 1
                if (choice.getIsSelected()) {
                    if (!choice.getIsCorrect()) {
                        ++numberOfWrongAnswer;
                    }
                }
            }
            //if there is no correct answer and no wrong answer then answer is correct
            if (numberOfAnswer == 0 && numberOfWrongAnswer == 0) mark = oneQuestionPoint;
                //Answer only consider to be correct if there is no wrong answer and have correct answer euqal to number of answer
            else if (numberOfWrongAnswer == 0 && numberOfCorrectAnswer == numberOfAnswer) mark += oneQuestionPoint;
        }
        return mark;
    }

    @Override
    @Transactional
    public QuizDTO submitQuiz(String applicationId, String userId, String quizId, QuizEntity quizEntity) {
        if (quizEntity == null) {
            quizEntity = getQuizToSave(quizId, applicationId, userId);
            //Validate that quiz still in progress
            if (!checkQuizTime(quizEntity)) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Quiz.TIME_UP));
            }
        }
        ApplicationEntity applicationEntity = applicationRepository.getById(applicationId);
        quizEntity.setMark(evaluateQuiz(quizEntity));
        double passMark = applicationEntity.getBoothJobPosition().getPassMark();
        if (quizEntity.getMark() < passMark) {
            applicationEntity.setTestStatus(TestStatus.PASS);
        } else {
            applicationEntity.setTestStatus(TestStatus.FAIL);
        }
        quizEntity.setIsSubmitted(true);
        applicationRepository.save(applicationEntity);
        return quizMapper.toDTO(quizRepository.save(quizEntity));
    }
}
