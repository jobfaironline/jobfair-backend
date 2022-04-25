package org.capstone.job_fair.controllers.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.dtos.company.job.questions.ChoicesDTO;
import org.capstone.job_fair.models.statuses.QuestionStatus;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponse {
    private String id;
    private String content;
    private Long createDate;
    private Long updateDate;
    private QuestionStatus status;
    private String jobPositionId;
    private List<ChoicesDTO> choicesList;
}
