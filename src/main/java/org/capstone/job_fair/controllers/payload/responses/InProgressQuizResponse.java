package org.capstone.job_fair.controllers.payload.responses;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.constants.Views;
import org.capstone.job_fair.models.dtos.attendant.cv.test.QuizQuestionDTO;
import org.capstone.job_fair.models.enums.QuizQuestionType;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InProgressQuizResponse {

    private String id;
    private Long beginTime;
    private Long endTime;
    private Integer duration;
    private String jobPositionTitle;
    private String applicationId;
    private String cvId;
    private String boothJobPositionId;
    private List<QuizQuestionResponse> questionList;



    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuizQuestionResponse {
        private String id;
        private String content;
        private QuizQuestionType type;
        private List<QuizChoiceResponse> choiceList;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuizChoiceResponse {
        private String id;
        private String content;
        private Boolean isSelected;
    }
}
