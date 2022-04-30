package org.capstone.job_fair.services.interfaces.attendant.cv.test;

import org.capstone.job_fair.models.dtos.attendant.cv.test.QuizDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.test.QuizQuestionDTO;

import java.util.List;
import java.util.Optional;

public interface QuizService {
    QuizDTO createQuiz(String applicationId, String jobPositionId,int numberOfQuestion);

    Optional<QuizDTO> getQuiz(String applicationId);

    QuizDTO saveQuiz(String applicationId, List<QuizQuestionDTO> answer);

    void submitQuiz(String applicationId);


    }
