package org.capstone.job_fair.repositories.job_fair;

import org.capstone.job_fair.models.entities.job_fair.AssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AssignmentRepository extends JpaRepository<AssignmentEntity, String> {
    Optional<AssignmentEntity> findByCompanyEmployeeAccountIdAndJobFairBoothId(String employeeId, String jobFairBoothId);
    List<AssignmentEntity> findByJobFairBoothJobFairIdAndJobFairBoothJobFairCompanyId(String jobFairId, String companyId);

    List<AssignmentEntity> findByJobFairBoothIdAndJobFairBoothJobFairCompanyId(String jobFairBoothId, String companyId);


}
