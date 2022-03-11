package org.capstone.job_fair.repositories.company;

import org.capstone.job_fair.models.entities.company.CompanyRegistrationEntity;
import org.capstone.job_fair.models.statuses.CompanyRegistrationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRegistrationRepository extends JpaRepository<CompanyRegistrationEntity, String> {
    Optional<CompanyRegistrationEntity> findByCompanyIdAndStatus(String companyId, CompanyRegistrationStatus status);

    List<CompanyRegistrationEntity> findAllByJobFairIdAndCompanyId(String jobFairId, String companyId);

    Page<CompanyRegistrationEntity> findAllByJobFairId(String jobFairId, Pageable pageable);

    Page<CompanyRegistrationEntity> findAllByJobFairIdAndStatusIn(String jobFairId, List<CompanyRegistrationStatus> status, Pageable pageable);

    Page<CompanyRegistrationEntity> findAllByCreatorIdAndStatusIn(String creatorId, List<CompanyRegistrationStatus> statusList, Pageable pageable);

    List<CompanyRegistrationEntity> findAllByJobFairIdAndCompanyIdAndStatus(String jobFairId, String companyId, CompanyRegistrationStatus status);

}
