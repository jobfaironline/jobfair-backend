package org.capstone.job_fair.repositories.company;

import org.capstone.job_fair.models.entities.company.CompanyBoothLayoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyBoothLayoutRepository extends JpaRepository<CompanyBoothLayoutEntity, String> {

    List<CompanyBoothLayoutEntity> findByCompanyBoothId(String companyBoothId);

    Optional<CompanyBoothLayoutEntity> findTopByCompanyBoothIdOrderByVersionDesc(String companyBoothId);
}
