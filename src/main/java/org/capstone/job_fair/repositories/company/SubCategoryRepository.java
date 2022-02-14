package org.capstone.job_fair.repositories.company;

import org.capstone.job_fair.models.entities.company.SubCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategoryEntity, Integer> {

}