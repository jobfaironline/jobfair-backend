package org.capstone.job_fair.services.interfaces.attendant.cv.test;

import org.capstone.job_fair.models.dtos.attendant.cv.test.QuizDTO;

import java.util.HashMap;

public interface QuizService {
    QuizDTO getOrCreateQuiz(String applicationId, String userId);


    QuizDTO saveQuiz(String applicationId, String userId, String quizId, HashMap<String, Boolean> answers);

    void submitQuiz(String applicationId, String userId, String quizId);


    }
