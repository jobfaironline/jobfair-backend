package org.capstone.job_fair.models.dtos.company.job.questions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.models.statuses.QuestionStatus;

import java.io.Serializable;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionsDTO implements Serializable {
    private String id;
    private String content;
    private Long createDate;
    private Long updateDate;
    private QuestionStatus status;
    private JobPositionDTO jobPosition;
    private List<ChoicesDTO> choicesList;
}
