package org.capstone.job_fair.models.dtos.attendant.cv.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.entities.attendant.cv.ApplicationEntity;
import org.capstone.job_fair.models.entities.attendant.cv.test.QuizQuestionEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizDTO implements Serializable {
    private String id;
    private Long beginTime;
    private Long endTime;
    private Boolean isSubmitted;
    private Double mark;
    @JsonIgnore
    private ApplicationEntity application;
    private String applicationId;
    private List<QuizQuestionDTO> questionList;
}
