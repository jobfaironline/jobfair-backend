package org.capstone.job_fair.repositories.company;

import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, String> {
    Optional<CompanyEntity> findByTaxId(String taxId);

    Integer countByEmail(String email);

    Integer countByTaxId(String taxId);

    Integer countById(String id);

    Integer countByIdAndTaxId(String id, String taxId);
}