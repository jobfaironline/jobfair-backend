package org.capstone.job_fair.controllers.attendant.cv;


import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.controllers.payload.requests.account.cv.SaveQuizRequest;
import org.capstone.job_fair.models.dtos.attendant.cv.test.QuizDTO;
import org.capstone.job_fair.services.interfaces.attendant.cv.test.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class QuizController {

    @Autowired
    private QuizService quizService;

    @GetMapping(ApiEndPoint.Quiz.QUIZ_ENDPOINT)
    public ResponseEntity<?> getQuiz(@RequestParam(name = "applicationId") String applicationId) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetailsImpl user = (UserDetailsImpl) securityContext.getAuthentication().getPrincipal();
        QuizDTO quizDTO = quizService.getOrCreateQuiz(applicationId, user.getId());
        return ResponseEntity.ok(quizDTO);
    }

    @PostMapping(ApiEndPoint.Quiz.QUIZ_ENDPOINT)
    public ResponseEntity<?> saveQuiz(@Validated @RequestBody SaveQuizRequest saveQuizRequest) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetailsImpl user = (UserDetailsImpl) securityContext.getAuthentication().getPrincipal();
        return ResponseEntity.ok(quizService.saveQuiz(saveQuizRequest.getApplicationId(), user.getId(), saveQuizRequest.getQuizId(), saveQuizRequest.getAnswers()));
    }

    @PostMapping(ApiEndPoint.Quiz.SUBMIT + "/{id}")
    public ResponseEntity<?> submitQuiz(@PathVariable("id") String id, @RequestParam(name = "applicationId") String applicationId) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetailsImpl user = (UserDetailsImpl) securityContext.getAuthentication().getPrincipal();
        quizService.submitQuiz(applicationId, user.getId(), id);
        return ResponseEntity.ok().build();
    }
}
