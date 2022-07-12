package org.capstone.job_fair.models.dtos.attendant.application;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ApplicationActivityDTO {
    @EqualsAndHashCode.Include
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
