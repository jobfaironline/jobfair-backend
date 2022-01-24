package org.capstone.job_fair.models.dtos.attendant.cv;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.enums.Qualification;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationDTO {
    private String id;
    private String subject;
    private String school;
    private Long fromDate;
    private Long toDate;
    private String achievement;
    private Qualification qualification;

}
