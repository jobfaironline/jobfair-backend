package org.capstone.job_fair.models.dtos.attendant.application;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ApplicationWorkHistoryDTO {
    @EqualsAndHashCode.Include
    private String id;
    private String position;
    private String company;
    private Long fromDate;
    private Long toDate;
    private Boolean isCurrentJob;
    private String description;
    private String descriptionKeyWord;
}
