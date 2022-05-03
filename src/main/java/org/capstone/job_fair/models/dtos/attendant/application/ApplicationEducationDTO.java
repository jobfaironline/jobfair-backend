package org.capstone.job_fair.models.dtos.attendant.application;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ApplicationEducationDTO {
    @EqualsAndHashCode.Include
    private String id;
    private String subject;
    private String school;
    private Long fromDate;
    private Long toDate;
    private String achievement;
}
