package org.capstone.job_fair.models.dtos.attendant.application;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ApplicationCertificationDTO {
    @EqualsAndHashCode.Include
    private String id;
    private String name;
    private String institution;
    private Long issueDate;
    private String certificationLink;
    private Long expiredDate;
    private Boolean doesNotExpired;
}
