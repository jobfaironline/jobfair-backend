package org.capstone.job_fair.repositories.company;

import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyEmployeeRepository extends JpaRepository<CompanyEmployeeEntity, String> {

    List<CompanyEmployeeEntity> findAllByCompanyIdAndAccountRoleId(String id, int roleId);

    Integer countByCompanyIdAndAccountStatusIn(String companyId, List<AccountStatus> status);

    Optional<CompanyEmployeeEntity> findByAccountId(String accountId);

    Optional<CompanyEmployeeEntity> findByAccountIdAndCompanyId(String accountID, String companyID);

    Integer countByCompanyId(String companyId);

    @Query("select c from CompanyEmployeeEntity c " +
            "where (c.account.firstname like concat('%', ?1, '%') or c.account.middlename like concat('%', ?2, '%') or c.account.lastname like concat('%', ?3, '%') or c.employeeId like concat('%', ?4, '%')) and c.company.id = ?5")
    Page<CompanyEmployeeEntity> findAllByAccountFirstnameContainsOrAccountMiddlenameContainsOrAccountLastnameContainsOrEmployeeIdContainsAndCompanyId
            (String firstName, String middleName, String lastName, String employeeId, String companyId, Pageable pageable);
}