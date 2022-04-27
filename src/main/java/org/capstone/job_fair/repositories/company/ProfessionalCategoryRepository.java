package org.capstone.job_fair.repositories.company;

import org.capstone.job_fair.models.entities.company.ProfessionCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessionalCategoryRepository extends JpaRepository<ProfessionCategoryEntity, Integer> {
}
