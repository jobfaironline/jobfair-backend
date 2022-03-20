package org.capstone.job_fair.models.dtos.attendant.cv;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.capstone.job_fair.models.dtos.company.job.RegistrationJobPositionDTO;
import org.capstone.job_fair.models.enums.Application;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ApplicationDTO {
    @EqualsAndHashCode.Include
    private String id;
    private String summary;
    private Long createDate;
    @JsonBackReference
    private CvDTO cvDTO;
    private Application status;
    @JsonBackReference
    private RegistrationJobPositionDTO registrationJobPositionDTO;
}
