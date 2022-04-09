package org.capstone.job_fair.repositories.job_fair;

import org.capstone.job_fair.models.entities.job_fair.LayoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LayoutRepository extends JpaRepository<LayoutEntity, String> {
    List<LayoutEntity> findByCompanyIdIsNull();

    Optional<LayoutEntity> findByIdAndCompanyId(String id, String companyId);

    List<LayoutEntity> findByCompanyId(String companyId);
}
