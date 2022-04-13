package org.capstone.job_fair.repositories.company;

import org.capstone.job_fair.models.entities.company.JobFairBoothEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobFairBoothRepository extends JpaRepository<JobFairBoothEntity, String> {
    @Query(
            value = "select * from company_booth where booth_id = :boothId AND order_id IN (select id from `order` where company_registration_id IN (select id from company_registration where job_fair_id = :jobFairId))",
            nativeQuery = true
    )
    Optional<JobFairBoothEntity> getCompanyBoothByJobFairIdAndBoothId(@Param("jobFairId") String jobFairId, @Param("boothId") String boothId);

    List<JobFairBoothEntity> findByJobFairId(String jobFairId);

    Integer deleteAllByJobFairIdAndBoothLayoutCompanyId(String jobFairId, String companyId);

}
