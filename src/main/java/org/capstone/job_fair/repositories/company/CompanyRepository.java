package org.capstone.job_fair.repositories.company;

import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, String> {
    Optional<CompanyEntity> findByTaxId(String taxId);

    Integer countByEmail(String email);

    Integer countByTaxId(String taxId);

    Integer countById(String id);

    Integer countByIdAndTaxId(String id, String taxId);

    List<CompanyEntity> findAllByNameContains(String name);

    @Query(value = "select a from CompanyEntity a where a.email LIKE :searchValue or a.name LIKE :searchValue")
    Page<CompanyEntity> findAllWithSearchValue(@Param("searchValue") String searchValue, Pageable pageable);

}