package org.capstone.job_fair.models.dtos.attendant.cv.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizQuestionDTO implements Serializable {
    @NotNull
    private String id;
    private String content;
    private String quizId;
    @Valid
    private List<QuizChoiceDTO> choiceList;

}
