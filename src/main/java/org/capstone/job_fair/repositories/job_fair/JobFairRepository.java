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

    @Query("select DISTINCT j from JobFairEntity j JOIN j.company.subCategories as c where (j.name like :name or j.name is null) and j.status = :status and (j.publicStartTime <= :currentTime and j.publicEndTime >= :currentTime) and (CAST(:subCategoryId as int ) = c.id OR :subCategoryId='')")
    Page<JobFairEntity> findJobFairForAttendant(
            @Param("name") String name,
            @Param("status") JobFairPlanStatus status,
            @Param("currentTime") long currentTime,
            @Param("subCategoryId") String subCategory,
            Pageable pageable);

    @Query("SELECT j FROM JobFairEntity j where (j.name like :name or j.name is null) and j.status = 1 and j.decorateStartTime < :now and j.publicEndTime > :now")
    Page<JobFairEntity> findInProgressJobFair(@Param("name") String name, @Param("now") long now, Pageable pageable);

    @Query("SELECT j FROM JobFairEntity j where (j.name like :name or j.name is null) and j.status = 1 and j.publicEndTime <= :now")
    Page<JobFairEntity> findPastJobFair(@Param("name") String name, @Param("now") long now, Pageable pageable);

    @Query("SELECT j FROM JobFairEntity j where (j.name like :name or j.name is null) and j.status = 1 and j.decorateStartTime >= :now")
    Page<JobFairEntity> findUpComingJobFair(@Param("name") String name, @Param("now") long now, Pageable pageable);





}
