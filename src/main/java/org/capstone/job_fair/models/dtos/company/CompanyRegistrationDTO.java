package org.capstone.job_fair.models.dtos.company;

import lombok.*;
import org.capstone.job_fair.models.dtos.company.job.RegistrationJobPositionDTO;
import org.capstone.job_fair.models.statuses.CompanyRegistrationStatus;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
public class CompanyRegistrationDTO implements Serializable {
    @EqualsAndHashCode.Include
    private String id;
    private long createDate;
    private String description;
    private String jobFairId;
    private String companyId;
    private CompanyRegistrationStatus status;
    private String cancelReason;
    private String adminMessage;
    private String authorizerId;
    private List<RegistrationJobPositionDTO> registrationJobPositions;
    private String creatorId;
}
