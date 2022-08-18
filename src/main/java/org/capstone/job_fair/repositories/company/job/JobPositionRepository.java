package org.capstone.job_fair.repositories.company.job;

import org.capstone.job_fair.models.entities.company.job.JobPositionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobPositionRepository extends JpaRepository<JobPositionEntity, String> {
    List<JobPositionEntity> findAllByCompanyId(String companyId);

    @Query("select j from JobPositionEntity j where j.company.id = :companyId and (:jobEntityId is null or j.jobTypeEntity.id = :jobEntityId) " + "and (:jobLevelId is null or j.jobLevel.id = :jobLevelId) " + "and (:jobTitle is null  or j.title like concat('%', :jobTitle, '%') )")
    Page<JobPositionEntity> findAllByCriteria(@Param("companyId") String companyId, @Param("jobEntityId") Integer jobTypeId, @Param("jobLevelId") Integer jobLevelId, @Param("jobTitle") String jobTitle, Pageable pageable);

    Optional<JobPositionEntity> findByIdAndCompanyId(String id, String companyId);

    Optional<JobPositionEntity> findFirstByTitleLikeAndCompanyId(String title, String companyId);
}