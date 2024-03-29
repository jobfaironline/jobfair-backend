package org.capstone.job_fair.models.dtos.attendant.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityDTO {
    private String id;
    private String name;
    private String functionTitle;
    private String organization;
    private Long fromDate;
    private Long toDate;
    private Boolean isCurrentActivity;
    private String description;
    private String descriptionKeyWord;

}
