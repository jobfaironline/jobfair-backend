package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.job.BoothJobPositionDTO;
import org.capstone.job_fair.models.statuses.CompanyRegistrationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface CompanyRegistrationService {
    CompanyRegistrationDTO createDraftCompanyRegistration(CompanyRegistrationDTO companyRegistrationDTO, List<BoothJobPositionDTO> jobPositions);

    void updateDraftCompanyRegistration(CompanyRegistrationDTO companyRegistrationDTO, List<BoothJobPositionDTO> jobPositions);

    void submitCompanyRegistration(String registrationId);

    void cancelCompanyRegistration(String registrationId, String cancelReason);

    void staffEvaluateCompanyRegistration(String companyRegistrationId, CompanyRegistrationStatus status, String message);

    List<CompanyRegistrationDTO> getCompanyRegistrationByJobFairIDAndCompanyID(String jobFairId, String companyId);

    Page<CompanyRegistrationDTO> getCompanyRegistrationOfAJobFair(String jobFairId, int offset, int pageSize, String sortBy, Sort.Direction direction);

    Page<CompanyRegistrationDTO> getCompanyRegistration(List<CompanyRegistrationStatus> statusList, int offset, int pageSize, String sortBy, Sort.Direction direction);

    Optional<CompanyRegistrationDTO> getCompanyLatestApproveRegistrationByJobFairIdAndCompanyId(String jobFairId, String companyId);

    Optional<CompanyRegistrationDTO> getById(String registrationId);

    Page<CompanyRegistrationDTO> getCompanyRegistrationByUserId(String userId, List<CompanyRegistrationStatus> statusList, int offset, int pageSize, String sortBy, Sort.Direction direction);

    Page<CompanyRegistrationDTO> getCompanyRegistrationByCompanyId(String companyId, List<CompanyRegistrationStatus> statusList, int offset, int pageSize, String sortBy, Sort.Direction direction);

    Page<CompanyRegistrationAdminDTO> getAllJobFairForAdmin(String companyName, String jobfairName, List<CompanyRegistrationStatus> statusList, int offset, int pageSize, String sortBy, Sort.Direction direction);

    Optional<CompanyRegistrationDTO> getCompanyLatestCompanyRegistrationByJobFairIdAndCompanyId(String jobFairId, String companyId);
}
