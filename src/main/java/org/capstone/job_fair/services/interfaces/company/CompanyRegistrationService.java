package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.CompanyRegistrationDTO;
import org.capstone.job_fair.models.dtos.company.job.RegistrationJobPositionDTO;
import org.capstone.job_fair.models.statuses.CompanyRegistrationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface CompanyRegistrationService {
    CompanyRegistrationDTO createDraftCompanyRegistration(CompanyRegistrationDTO companyRegistrationDTO, List<RegistrationJobPositionDTO> jobPositions);

    void updateDraftCompanyRegistration(CompanyRegistrationDTO companyRegistrationDTO, List<RegistrationJobPositionDTO> jobPositions);

    void submitCompanyRegistration(String registrationId);

    void cancelCompanyRegistration(String registrationId, String cancelReason);

    void staffEvaluateCompanyRegistration(String companyRegistrationId, CompanyRegistrationStatus status, String message);

    List<CompanyRegistrationDTO> getCompanyRegistrationByJobFairIDAndCompanyID(String jobFairId, String companyId);

    Page<CompanyRegistrationDTO> getCompanyRegistrationOfAJobFair(String jobFairId, int offset, int pageSize, String sortBy, Sort.Direction direction);

    Optional<CompanyRegistrationDTO> getCompanyLatestApproveRegistrationByJobFairIdAndCompanyId(String jobFairId, String companyId);

    Optional<CompanyRegistrationDTO> getById(String registrationId);

    Page<CompanyRegistrationDTO> getCompanyRegistrationByUserId(String userId, List<CompanyRegistrationStatus> statusList, int offset, int pageSize, String sortBy, Sort.Direction direction);
}
