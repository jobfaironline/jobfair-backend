package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.CompanyRegistrationDTO;
import org.capstone.job_fair.models.dtos.company.job.RegistrationJobPositionDTO;
import org.capstone.job_fair.models.statuses.CompanyRegistrationStatus;

import java.util.List;
import java.util.Optional;

public interface CompanyRegistrationService {
    CompanyRegistrationDTO createDraftCompanyRegistration(CompanyRegistrationDTO companyRegistrationDTO, List<RegistrationJobPositionDTO> jobPositions);

    void updateDraftCompanyRegistration(CompanyRegistrationDTO companyRegistrationDTO, List<RegistrationJobPositionDTO> jobPositions);

    void submitCompanyRegistration(String registrationId);

    void cancelCompanyRegistration(String registrationId, String cancelReason);

    void staffEvaluateCompanyRegistration(String companyRegistrationId, CompanyRegistrationStatus status, String message);

    List<CompanyRegistrationDTO> getCompanyRegistrationByJobFairIDAndCompanyID(String jobFairId, String companyId);

    List<CompanyRegistrationDTO> getCompanyRegistrationOfAJobFair(String jobFairId);

    Optional<CompanyRegistrationDTO> getCompanyLatestApproveRegistrationByJobFairIdAndCompanyId(String jobFairId, String companyId);

}
