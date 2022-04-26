package org.capstone.job_fair.controllers.payload.requests.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateQuestionCSVRequest {
    @NotBlank
    @XSSConstraint
    private String content;
    @NotNull
    private String jobPositionId;
    @NotBlank
    @XSSConstraint
    private String answer1Content;
    private Boolean answer1IsCorrect;
    @NotBlank
    @XSSConstraint
    private String answer2Content;
    private Boolean answer2IsCorrect;
    @NotBlank
    @XSSConstraint
    private String answer3Content;
    private Boolean answer3IsCorrect;
    @NotBlank
    @XSSConstraint
    private String answer4Content;
    private Boolean answer4IsCorrect;
}
