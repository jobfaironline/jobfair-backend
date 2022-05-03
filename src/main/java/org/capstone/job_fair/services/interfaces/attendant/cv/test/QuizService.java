package org.capstone.job_fair.services.interfaces.attendant.cv.test;

import org.capstone.job_fair.models.dtos.attendant.cv.test.QuizDTO;

import java.util.HashMap;
import java.util.Optional;

public interface QuizService {
    QuizDTO createQuiz(String applicationId, String userId);
    Optional<QuizDTO> getQuizById(String id, String applicationId, String userid);


    QuizDTO saveQuiz(String applicationId, String userId, String quizId, HashMap<String, Boolean> answers);

    QuizDTO submitQuiz(String applicationId, String userId, String quizId);


    }
