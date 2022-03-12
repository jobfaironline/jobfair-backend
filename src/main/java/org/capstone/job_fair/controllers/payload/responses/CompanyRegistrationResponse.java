package org.capstone.job_fair.controllers.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.dtos.company.job.RegistrationJobPositionDTO;
import org.capstone.job_fair.models.statuses.CompanyRegistrationStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRegistrationResponse {
    private String id;
    private long createDate;
    private String description;
    private String jobFairId;
    private String companyId;
    private CompanyRegistrationStatus status;
    private String cancelReason;
    private String rejectReason;
    private String authorizerId;
    private List<RegistrationJobPositionDTO> registrationJobPositions;
    private String creatorId;
    private String companyName;
    private String jobfairName;
}
