package org.capstone.job_fair.controllers.attendant.quiz;


import com.fasterxml.jackson.annotation.JsonView;
import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.Views;
import org.capstone.job_fair.controllers.payload.requests.account.cv.SaveQuizRequest;
import org.capstone.job_fair.controllers.payload.responses.InProgressQuizResponse;
import org.capstone.job_fair.models.dtos.attendant.test.QuizDTO;
import org.capstone.job_fair.services.interfaces.attendant.quiz.QuizService;
import org.capstone.job_fair.services.mappers.attendant.quiz.QuizMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizMapper quizMapper;

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @GetMapping(ApiEndPoint.Quiz.IN_PROGRESS + "/{id}")
    public ResponseEntity<?> getInProgressQuizByIdForAttendant(@PathVariable("id") String id) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetailsImpl user = (UserDetailsImpl) securityContext.getAuthentication().getPrincipal();
        Optional<QuizDTO> quizDTOOpt = quizService.getQuizById(id, user.getId(), true);
        if (quizDTOOpt.isPresent()){
            QuizDTO quizDTO = quizDTOOpt.get();
            InProgressQuizResponse response = quizMapper.toResponse(quizDTO);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @GetMapping(ApiEndPoint.Quiz.DONE + "/{id}")
    public ResponseEntity<?> getDoneQuizByIdForAttendant(@PathVariable("id") String id) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetailsImpl user = (UserDetailsImpl) securityContext.getAuthentication().getPrincipal();
        Optional<QuizDTO> quizDTO = quizService.getQuizById(id, user.getId(), false);
        return quizDTO.isPresent() ? ResponseEntity.ok(quizDTO.get()) : ResponseEntity.notFound().build();
    }


    @JsonView(Views.Public.class)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @PostMapping(ApiEndPoint.Quiz.QUIZ_ENDPOINT )
    public ResponseEntity<?> createQuizForAttendant(@RequestParam(name = "applicationId") String applicationId) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetailsImpl user = (UserDetailsImpl) securityContext.getAuthentication().getPrincipal();
        QuizDTO quizDTO = quizService.createQuiz(applicationId, user.getId());
        return ResponseEntity.ok(quizDTO);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @PostMapping(ApiEndPoint.Quiz.SAVE + "/{id}" )
    public ResponseEntity<?> saveQuiz(@PathVariable("id") String quizId, @Validated @RequestBody SaveQuizRequest saveQuizRequest) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetailsImpl user = (UserDetailsImpl) securityContext.getAuthentication().getPrincipal();
        return ResponseEntity.ok(quizService.saveQuiz(user.getId(), quizId, saveQuizRequest.getAnswers()));
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @PostMapping(ApiEndPoint.Quiz.SUBMIT + "/{id}")
    public ResponseEntity<?> submitQuiz(@PathVariable("id") String id) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetailsImpl user = (UserDetailsImpl) securityContext.getAuthentication().getPrincipal();
        QuizDTO dto = quizService.submitQuiz(user.getId(), id, null);
        return ResponseEntity.ok(dto);
    }
}
