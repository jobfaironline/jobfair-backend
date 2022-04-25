package org.capstone.job_fair.models.dtos.attendant.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificationDTO {
    private String id;
    private String name;
    private String institution;
    private Long issueDate;
    private Long expiredDate;
    private Boolean doesNotExpired;
    private String certificationLink;
}
