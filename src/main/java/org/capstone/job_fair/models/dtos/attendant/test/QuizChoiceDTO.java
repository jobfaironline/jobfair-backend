package org.capstone.job_fair.models.dtos.attendant.test;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.constants.Views;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizChoiceDTO implements Serializable {
    @NotNull
    @JsonView(Views.Public.class)
    private String id;
    @JsonView(Views.Public.class)
    private String content;
    @JsonView(Views.Internal.class)
    private Boolean isCorrect;
    @NotNull
    @JsonView(Views.Public.class)
    private Boolean isSelected;
}
