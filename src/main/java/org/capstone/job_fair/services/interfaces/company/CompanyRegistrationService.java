package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.CompanyRegistrationDTO;
import org.capstone.job_fair.models.dtos.company.job.RegistrationJobPositionDTO;
import org.capstone.job_fair.models.entities.company.CompanyRegistrationEntity;
import org.capstone.job_fair.models.statuses.CompanyRegistrationStatus;

import java.util.List;

public interface CompanyRegistrationService {
    void createDraftCompanyRegistration(CompanyRegistrationDTO companyRegistrationDTO, List<RegistrationJobPositionDTO> jobPositions);

    void submitCompanyRegistration(String registrationId);

    void cancelCompanyRegistration(String registrationId, String cancelReason);

    void staffEvaluateCompanyRegistration(String companyRegistrationId, CompanyRegistrationStatus status, String message);

    String getCompanyIdInSecurityContext();

    List<CompanyRegistrationDTO> getAllOwnCompanyRegistrationOfAJobFair(String jobFairId, String companyId);
}
