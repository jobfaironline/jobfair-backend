package org.capstone.job_fair.repositories.job_fair.job_fair_booth;

import org.capstone.job_fair.models.entities.job_fair.booth.AssignmentEntity;
import org.capstone.job_fair.models.enums.AssignmentType;
import org.capstone.job_fair.models.statuses.JobFairPlanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AssignmentRepository extends JpaRepository<AssignmentEntity, String> {
    List<AssignmentEntity> findByCompanyEmployeeAccountIdAndJobFairBoothId(String employeeId, String jobFairBoothId);

    List<AssignmentEntity> findByJobFairBoothJobFairIdAndJobFairBoothJobFairCompanyId(String jobFairId, String companyId);

    List<AssignmentEntity> findByJobFairBoothIdAndJobFairBoothJobFairCompanyId(String jobFairBoothId, String companyId);

    @Query("select count(distinct a.jobFairBooth) from AssignmentEntity a where a.jobFairBooth.jobFair.id = ?1")
    Integer countDistinctJobFairBoothByJobFairBoothJobFairId(String jobFairId);

    Integer countByJobFairBoothJobFairId(String jobFairId);

    @Modifying
    Integer deleteByJobFairBoothId(String jobFairBoothId);

    Page<AssignmentEntity> findByCompanyEmployeeAccountIdAndJobFairBoothJobFairStatus(String employeeId, JobFairPlanStatus status, Pageable pageable);

    Page<AssignmentEntity> findByCompanyEmployeeAccountIdAndJobFairBoothJobFairStatusAndType(String employeeId, JobFairPlanStatus status, AssignmentType type, Pageable pageable);

    List<AssignmentEntity> findByJobFairBoothIdAndType(String jobFairBoothId, AssignmentType type);

    @Query("select a from AssignmentEntity a where a.companyEmployee.accountId = ?1 and a.jobFairBooth.jobFair.id = ?2")
    List<AssignmentEntity> findByEmployeeId(String employeeId, String jobFairId);
}
