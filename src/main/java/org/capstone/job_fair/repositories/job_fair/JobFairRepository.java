package org.capstone.job_fair.repositories.job_fair;

import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.statuses.JobFairPlanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobFairRepository extends JpaRepository<JobFairEntity, String> {
    Optional<JobFairEntity> findByIdAndCompanyId(String jobFairId, String companyId);

    List<JobFairEntity> findByCompanyIdAndStatus(String companyId, JobFairPlanStatus status);

    @Query("select j from JobFairEntity j where (j.name like :name or j.name is null) and j.company.id = :companyId and (j.status = :status or :status is null)")
    Page<JobFairEntity> findByNameLikeOrNameIsNullAndCompanyIdAndStatus(@Param("name") String name, @Param("companyId") String companyId, @Param("status") JobFairPlanStatus status, Pageable pageable);

    @Query("select j from JobFairEntity j where (j.name like :name or j.name is null) and (j.status = :status or :status is null)")
    Page<JobFairEntity> findByNameLikeOrNameIsNullAndStatus(@Param("name") String name, @Param("status") JobFairPlanStatus status, Pageable pageable);

    @Query("select j from JobFairEntity j where (j.name like :name or j.name is null) and j.status = :status and (j.publicStartTime <= :currentTime and j.publicEndTime >= :currentTime)")
    Page<JobFairEntity> findJobFairForAttendant(
            @Param("name") String name,
            @Param("status") JobFairPlanStatus status,
            @Param("currentTime") long currentTime,
            Pageable pageable);

    @Query("SELECT j FROM JobFairEntity j where j.name like :name and j.status = 1 and j.decorateStartTime < :now and j.publicEndTime > :now")
    Page<JobFairEntity> findInProgressJobFair(@Param("name") String name, @Param("now") long now, Pageable pageable);

    @Query("SELECT j FROM JobFairEntity j where j.name like :name and j.status = 1 and j.publicEndTime <= :now")
    Page<JobFairEntity> findPastJobFair(@Param("name") String name, @Param("now") long now, Pageable pageable);

    @Query("SELECT j FROM JobFairEntity j where j.name like :name and j.status = 1 and j.decorateStartTime >= :now")
    Page<JobFairEntity> findUpComingJobFair(@Param("name") String name, @Param("now") long now, Pageable pageable);

    List<JobFairEntity> findByStatus(JobFairPlanStatus status);

    @Query(value = "SELECT * FROM job_fair WHERE id IN (SELECT distinct(job_fair_id) from job_fair_booth WHERE id in (SELECT job_fair_booth_id FROM assignment WHERE company_employee_id = :companyEmployeeId) and job_fair_id IS NOT NULL) AND status =  1 AND name LIKE :jobFairName",
            countQuery = "SELECT COUNT(*) FROM job_fair WHERE id IN (SELECT distinct(job_fair_id) from job_fair_booth WHERE id in (SELECT job_fair_booth_id FROM assignment WHERE company_employee_id = :companyEmployeeId) and job_fair_id IS NOT NULL) AND status =  1",
            nativeQuery = true)
    Page<JobFairEntity> findJobFairThatEmployeeHasAssignment(@Param("companyEmployeeId") String companyEmployeeId, @Param("jobFairName") String jobFairName, Pageable pageable);

    @Query("SELECT j FROM JobFairEntity j where j.status = 1 and j.createTime >= :from and j.createTime <= :to")
    List<JobFairEntity> findJobFairForAdminInRange(@Param("from") long from, @Param("to") long to);

}
