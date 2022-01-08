package org.capstone.job_fair.repositories.company;

import org.capstone.job_fair.models.entities.company.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RepositoryRestResource(path = "categories")
public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {
}