package org.capstone.job_fair.controllers.payload.requests.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.models.dtos.company.job.questions.ChoicesDTO;
import org.capstone.job_fair.models.dtos.company.job.questions.QuestionsDTO;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateQuestionsRequest {

    @NotBlank
    @XSSConstraint
    private String content;
    @NotNull
    private String jobPositionId;
    @Valid
    private List<Choice> choicesList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Choice {
        @NotBlank
        @XSSConstraint
        private String content;
        private Byte isCorrect;
    }
}
