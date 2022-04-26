package org.capstone.job_fair.controllers.company;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.QuestionConstant;
import org.capstone.job_fair.controllers.payload.requests.company.CreateQuestionsRequest;
import org.capstone.job_fair.controllers.payload.requests.company.UpdateQuestionsRequest;
import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.models.dtos.company.job.questions.ChoicesDTO;
import org.capstone.job_fair.models.dtos.company.job.questions.QuestionsDTO;
import org.capstone.job_fair.models.statuses.QuestionStatus;
import org.capstone.job_fair.services.interfaces.company.question.QuestionsService;
import org.capstone.job_fair.services.mappers.company.question.QuestionsMapper;
import org.capstone.job_fair.validators.XSSConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class QuestionsController {

    @Autowired
    private QuestionsMapper questionsMapper;

    @Autowired
    private QuestionsService questionsService;


    @PostMapping(ApiEndPoint.Questions.QUESTION)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    public ResponseEntity<?> createQuestions(@Validated @RequestBody CreateQuestionsRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QuestionsDTO questionsDTO = new QuestionsDTO();
        JobPositionDTO jobPositionDTO = new JobPositionDTO();
        jobPositionDTO.setId(request.getJobPositionId());
        questionsDTO.setJobPosition(jobPositionDTO);
        questionsDTO.setContent(request.getContent());
        List<ChoicesDTO> choicesDTOList = request.getChoicesList().stream().map(choice -> {
            return new ChoicesDTO(null, choice.getContent(), choice.isCorrect(), null);
        }).collect(Collectors.toList());
        questionsDTO.setChoicesList(choicesDTOList);
        questionsDTO = questionsService.createQuestion(questionsDTO, userDetails.getId(), userDetails.getCompanyId());
        return ResponseEntity.ok(questionsMapper.toResponse(questionsDTO));
    }

    @PutMapping(ApiEndPoint.Questions.QUESTION)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    public ResponseEntity<?> updateQuestion(@Validated @RequestBody UpdateQuestionsRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QuestionsDTO questionsDTO = new QuestionsDTO();
        JobPositionDTO jobPositionDTO = new JobPositionDTO();
        jobPositionDTO.setId(request.getJobPositionId());
        questionsDTO.setJobPosition(jobPositionDTO);
        questionsDTO.setContent(request.getContent());
        questionsDTO.setId(request.getId());
        List<ChoicesDTO> choicesDTOList = null;
        if (request.getChoicesList() != null) choicesDTOList = request.getChoicesList().stream().map(choice -> {
            return new ChoicesDTO(null, choice.getContent(), choice.isCorrect(), null);
        }).collect(Collectors.toList());

        questionsDTO.setChoicesList(choicesDTOList);
        questionsDTO = questionsService.updateQuestion(questionsDTO, userDetails.getCompanyId());
        return ResponseEntity.ok(questionsMapper.toResponse(questionsDTO));
    }

    @GetMapping(ApiEndPoint.Questions.QUESTION + "/{id}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    public ResponseEntity<?> getQuestionById(@PathVariable("id") String id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<QuestionsDTO> questionsDTOOptional = questionsService.getQuestionById(id, userDetails.getCompanyId());
        if (!questionsDTOOptional.isPresent()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(questionsMapper.toResponse(questionsDTOOptional.get()));
    }

    @GetMapping(ApiEndPoint.Questions.QUESTION)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    public ResponseEntity<?> getQuestionByCriteria(
            @RequestParam(value = "offset", defaultValue = QuestionConstant.DEFAULT_SEARCH_OFFSET_VALUE) int offset,
            @RequestParam(value = "pageSize", required = false, defaultValue = QuestionConstant.DEFAULT_SEARCH_PAGE_SIZE_VALUE) int pageSize,
            @RequestParam(value = "sortBy", required = false, defaultValue = QuestionConstant.DEFAULT_SEARCH_SORT_BY_VALUE) String sortBy,
            @RequestParam(value = "direction", required = false, defaultValue = QuestionConstant.DEFAULT_SEARCH_SORT_DIRECTION) Sort.Direction direction,
            @XSSConstraint @RequestParam(value = "questionContent", required = false, defaultValue = QuestionConstant.DEFAULT_SEARCH_QUESTION_CONTENT) String searchContent,
            @RequestParam(value = "fromDate", required = false, defaultValue = QuestionConstant.DEFAULT_FROM_DATE) long fromDate,
            @RequestParam(value = "toDate", required = false, defaultValue = QuestionConstant.DEFAULT_TO_DATE) long toDate,
            @RequestParam(value = "status", required = false, defaultValue = QuestionConstant.DEFAULT_QUESTION_STATUS) QuestionStatus status) {

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<QuestionsDTO> questionsDTOPage = questionsService.getQuestionsByCriteria(userDetails.getCompanyId(), searchContent, fromDate, toDate, status, pageSize, offset, sortBy, direction);
        if (questionsDTOPage.getTotalElements() == 0) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(questionsDTOPage.map(dto -> questionsMapper.toResponse(dto)));
    }

    @DeleteMapping(ApiEndPoint.Questions.QUESTION + "/{id}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> deleteQuestion(@PathVariable("id") String id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QuestionsDTO dto = questionsService.deleteQuestion(id, userDetails.getCompanyId());
        return ResponseEntity.ok(questionsMapper.toResponse(dto));
    }

    @GetMapping(ApiEndPoint.Questions.BY_JOB_POSITION + "/{id}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    public ResponseEntity<?> getAllQuestionsOfJobPosition(@PathVariable("id") String jobPositionId,
                                                          @RequestParam(value = "offset", defaultValue = QuestionConstant.DEFAULT_SEARCH_OFFSET_VALUE) int offset,
                                                          @RequestParam(value = "pageSize", required = false, defaultValue = QuestionConstant.DEFAULT_SEARCH_PAGE_SIZE_VALUE) int pageSize,
                                                          @RequestParam(value = "sortBy", required = false, defaultValue = QuestionConstant.DEFAULT_SEARCH_SORT_BY_VALUE) String sortBy,
                                                          @RequestParam(value = "direction", required = false, defaultValue = QuestionConstant.DEFAULT_SEARCH_SORT_DIRECTION) Sort.Direction direction,
                                                          @RequestParam(value = "status", required = false, defaultValue = QuestionConstant.DEFAULT_QUESTION_STATUS) QuestionStatus status,
                                                          @XSSConstraint @RequestParam(value = "questionContent", required = false, defaultValue = QuestionConstant.DEFAULT_SEARCH_QUESTION_CONTENT) String searchContent) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<QuestionsDTO> dtoPage = questionsService.getQuestionByJobPosition(userDetails.getCompanyId(), jobPositionId, searchContent, status, offset, pageSize, sortBy, direction);
        if (dtoPage.getTotalElements() == 0) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(dtoPage.map(dto -> questionsMapper.toResponse(dto)));
    }

    @PostMapping(ApiEndPoint.Questions.UPLOAD_CSV + "/{id}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    public ResponseEntity<?> createMultipleQuestionsFromCSVFile(@PathVariable("id") String jobPositionId, @RequestPart("file") MultipartFile file) throws IOException {
        List<QuestionsDTO> result = questionsService.createNewJobPositionsFromCSVFile(file, jobPositionId);
        return ResponseEntity.ok(result);
    }


}
