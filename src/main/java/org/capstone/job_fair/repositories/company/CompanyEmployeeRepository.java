package org.capstone.job_fair.repositories.company;

import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyEmployeeRepository extends JpaRepository<CompanyEmployeeEntity, String> {

    List<CompanyEmployeeEntity> findAllByCompanyId(String id);

    Integer countByCompanyIdAndAccountStatusIn(String companyId, List<AccountStatus> status);

    Optional<CompanyEmployeeEntity> findByAccountId(String accountId);
}