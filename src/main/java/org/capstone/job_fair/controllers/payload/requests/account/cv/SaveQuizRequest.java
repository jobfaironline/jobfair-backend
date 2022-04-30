package org.capstone.job_fair.controllers.payload.requests.account.cv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.dtos.attendant.cv.test.QuizQuestionDTO;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveQuizRequest {
    @NotBlank
    private String applicationId;
    @Valid
    private List<QuizQuestionDTO> quizQuestionDTOList;
}
