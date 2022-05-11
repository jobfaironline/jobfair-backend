package org.capstone.job_fair.models.dtos.attendant.test;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.constants.Views;
import org.capstone.job_fair.models.dtos.attendant.application.ApplicationDTO;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonView(Views.Public.class)
public class QuizDTO implements Serializable {
    private String id;
    private Long beginTime;
    private Long endTime;
    private Boolean isSubmitted;
    private Double mark;
    private ApplicationDTO application;
    private List<QuizQuestionDTO> questionList;
    private Long submitTime;
}
