package org.capstone.job_fair.repositories.company.layout;

import org.capstone.job_fair.models.entities.company.layout.DecoratorBoothLayoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DecoratorBoothLayoutRepository extends JpaRepository<DecoratorBoothLayoutEntity, String> {

    List<DecoratorBoothLayoutEntity> findByCompanyEmployeeAccountId(String accountId);
}
