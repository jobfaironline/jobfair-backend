package org.capstone.job_fair.repositories.company.layout;

import org.capstone.job_fair.models.entities.company.layout.DecoratorBoothLayoutVideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DecorateBoothLayoutVideoRepository extends JpaRepository<DecoratorBoothLayoutVideoEntity, String> {
}
