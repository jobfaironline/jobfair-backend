package org.capstone.job_fair.models.dtos.attendant.cv.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizChoiceDTO implements Serializable {
    @NotNull
    private String id;
    private String content;
    private Boolean isCorrect;
    @NotNull
    private Boolean isSelected;
    private String quizQuestionId;
    @JsonIgnore
    private String originChoiceId;
}
