package org.capstone.job_fair.models.dtos.attendant.cv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CvActivityDTO implements Serializable {

    private String id;
    private String name;
    private String functionTitle;
    private String organization;
    private Long fromDate;
    private Long toDate;
    private Long isCurrentActivity;
    private String description;
}
