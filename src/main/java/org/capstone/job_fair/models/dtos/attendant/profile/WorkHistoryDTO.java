package org.capstone.job_fair.models.dtos.attendant.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkHistoryDTO {
    private String id;
    private String position;
    private String company;
    private Long fromDate;
    private Long toDate;
    private Boolean isCurrentJob;
    private String description;
}
