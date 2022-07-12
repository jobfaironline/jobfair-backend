package org.capstone.job_fair.models.dtos.attendant.cv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CvWorkHistoryDTO implements Serializable {
    private String id;
    private String position;
    private String company;
    private Long fromDate;
    private Long toDate;
    private Boolean isCurrentJob;
    private String description;
    private String descriptionKeyWord;
}
