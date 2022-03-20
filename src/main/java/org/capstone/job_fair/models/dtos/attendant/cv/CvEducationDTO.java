package org.capstone.job_fair.models.dtos.attendant.cv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CvEducationDTO implements Serializable {
    private String id;
    private String subject;
    private String school;
    private Long fromDate;
    private Long toDate;
    private String achievement;
    private CvDTO cv;
    private Integer qualificationId;

}
