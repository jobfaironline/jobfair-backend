package org.capstone.job_fair.repositories.company;

import org.capstone.job_fair.models.entities.company.CompanyBoothLayoutVideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyBoothLayoutVideoRepository extends JpaRepository<CompanyBoothLayoutVideoEntity, String> {
}
