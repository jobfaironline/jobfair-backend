package org.capstone.job_fair.repositories.job_fair;

import org.capstone.job_fair.models.entities.job_fair.CompanyJobFairStatusEntity;
import org.capstone.job_fair.models.statuses.JobFairCompanyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyJobFairStatusRepository extends JpaRepository<CompanyJobFairStatusEntity, String> {

    Page<CompanyJobFairStatusEntity> getAllByCompanyId(String companyId, Pageable pageable);

    Page<CompanyJobFairStatusEntity> getByStatusAndCompanyId(JobFairCompanyStatus status, String companyId, Pageable pageable);
}
