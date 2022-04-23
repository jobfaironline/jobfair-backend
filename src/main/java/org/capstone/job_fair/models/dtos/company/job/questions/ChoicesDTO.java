package org.capstone.job_fair.models.dtos.company.job.questions;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChoicesDTO implements Serializable {
    private String id;
    private String content;
    private boolean isCorrect;
    private String questionId;
}
