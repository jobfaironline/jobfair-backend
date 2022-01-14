package org.capstone.job_fair.models.dtos.attendant.cv;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationDTO {
    private String subject;
    private String school;
    private String qualificationId;
    private Long fromDate;
    private Long toDate;
    private String achievement;
    private String cvID;
}
