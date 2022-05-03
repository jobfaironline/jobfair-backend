package org.capstone.job_fair.models.dtos.attendant.cv.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.constants.Views;
import org.capstone.job_fair.models.entities.attendant.application.ApplicationEntity;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizDTO implements Serializable {
    @JsonView(Views.Public.class)
    private String id;
    @JsonView(Views.Public.class)
    private Long beginTime;
    @JsonView(Views.Public.class)
    private Long endTime;
    @JsonView(Views.Public.class)
    private Boolean isSubmitted;
    @JsonView(Views.Public.class)
    private Double mark;
    @JsonIgnore
    private ApplicationEntity application;
    @JsonView(Views.Public.class)
    private String applicationId;
    @JsonView(Views.Public.class)
    private List<QuizQuestionDTO> questionList;
    @JsonView(Views.Public.class)
    private Long submitTime;
}
