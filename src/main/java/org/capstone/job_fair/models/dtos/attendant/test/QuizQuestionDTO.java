package org.capstone.job_fair.models.dtos.attendant.test;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.constants.Views;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonView(Views.Public.class)
public class QuizQuestionDTO implements Serializable {
    @NotNull
    private String id;
    private String content;
    private String quizId;
    @Valid
    private List<QuizChoiceDTO> choiceList;

}
