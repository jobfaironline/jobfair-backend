package org.capstone.job_fair.repositories.company.misc;

import org.capstone.job_fair.models.entities.company.misc.ProfessionCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<ProfessionCategoryEntity, String> {
}