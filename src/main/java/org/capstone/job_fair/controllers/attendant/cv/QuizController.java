package org.capstone.job_fair.controllers.attendant.cv;


import io.swagger.annotations.Api;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.controllers.payload.requests.account.cv.SaveQuizRequest;
import org.capstone.job_fair.models.dtos.attendant.cv.test.QuizDTO;
import org.capstone.job_fair.services.interfaces.attendant.cv.test.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class QuizController {

    @Autowired
    private QuizService quizService;

    @GetMapping(ApiEndPoint.Quiz.QUIZ_ENDPOINT)
    public ResponseEntity<?> createQuiz(@RequestParam(name = "applicationId") String applicationId, @RequestParam( name = "jobPositionId") String jobPositionId, @RequestParam(name = "numberOfQuestion") int numberOfQuestion) {
        Optional<QuizDTO> dtoOptional = quizService.getQuiz(applicationId);
        return dtoOptional.isPresent() ? ResponseEntity.ok(dtoOptional.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping(ApiEndPoint.Quiz.QUIZ_ENDPOINT)
    public ResponseEntity<?> saveQuiz(@Validated @RequestBody SaveQuizRequest saveQuizRequest) {
        return ResponseEntity.ok(quizService.saveQuiz(saveQuizRequest.getApplicationId(), saveQuizRequest.getQuizQuestionDTOList()));
    }

    @PostMapping(ApiEndPoint.Quiz.SUBMIT + "/{id}")
    public ResponseEntity<?> submitQuiz(@PathVariable("id") String id){
        quizService.submitQuiz(id);
        return ResponseEntity.ok().build();
    }
}
