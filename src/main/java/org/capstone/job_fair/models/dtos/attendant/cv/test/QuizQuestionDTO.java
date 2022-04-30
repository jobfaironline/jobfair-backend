package org.capstone.job_fair.models.dtos.attendant.cv.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.entities.attendant.cv.test.QuizChoiceEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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
