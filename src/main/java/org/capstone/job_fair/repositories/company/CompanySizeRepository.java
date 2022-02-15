package org.capstone.job_fair.repositories.company;

import org.capstone.job_fair.models.entities.company.CompanySizeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanySizeRepository extends JpaRepository<CompanySizeEntity, Integer> {
    Integer countById(int id);
}