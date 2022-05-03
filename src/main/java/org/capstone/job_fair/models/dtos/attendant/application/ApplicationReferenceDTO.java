package org.capstone.job_fair.models.dtos.attendant.application;

import lombok.*;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ApplicationReferenceDTO {
    @EqualsAndHashCode.Include
    private String id;
    private String fullName;
    private String position;
    private String company;
    private String email;
    private String phoneNumber;
}
