package org.capstone.job_fair.services.interfaces.attendant.cv.test;

import org.capstone.job_fair.models.dtos.attendant.cv.test.QuizDTO;
import org.capstone.job_fair.models.entities.attendant.cv.test.QuizEntity;

import java.util.HashMap;
import java.util.Optional;

public interface QuizService {
    QuizDTO createQuiz(String applicationId, String userId);
    Optional<QuizDTO> getQuizById(String id, String userid, boolean isDoing);


    QuizDTO saveQuiz(String userId, String quizId, HashMap<String, Boolean> answers);

    QuizDTO submitQuiz(String userId, String quizId, QuizEntity entity);


    }
