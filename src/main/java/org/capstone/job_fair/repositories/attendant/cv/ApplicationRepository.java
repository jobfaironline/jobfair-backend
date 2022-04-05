package org.capstone.job_fair.repositories.attendant.cv;

import org.capstone.job_fair.models.entities.attendant.cv.ApplicationEntity;
import org.capstone.job_fair.models.enums.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, String> {

    @Query("select a from ApplicationEntity a " +
            "where a.createDate between ?1 and ?2 and a.status = ?3 and a.cv.attendant.accountId = ?4")
    Page<ApplicationEntity> findAllByCreateDateBetweenAndStatusAndCvAttendantAccountId(long fromDate, long toDate, ApplicationStatus status, String id, Pageable pageable);

    @Query("select a from ApplicationEntity a " +
            "where a.cv.attendant.accountId = :accountId and " +
            "a.createDate between :fromDate and :toDate and " +
            "a.status in :statusList and " +
            "a.boothJobPosition.title like concat('%',:jobPositionName,'%') and " +
            "a.boothJobPosition.jobFairBooth.jobFair.name like concat('%',:jobFairName,'%')")
    Page<ApplicationEntity> findAllApplicationOfAttendantByCriteria(@Param("accountId") String acocuntId,
                                                                    @Param("fromDate") Long fromDate,
                                                                    @Param("toDate") Long toDate,
                                                                    @Param("statusList") List<ApplicationStatus> statusList,
                                                                    @Param("jobPositionName") String jobPositionName,
                                                                    @Param("jobFairName") String jobFairName,
                                                                    Pageable pageable);

    @Query("select a from ApplicationEntity a " +
            "where a.boothJobPosition.jobFairBooth.jobFair.company.id = :companyId " +
            "and a.boothJobPosition.id = :jobPositionId and a.status in :statusList")
    Page<ApplicationEntity> findAllApplicationOfCompanyByJobPositionIdAndStatusIn(@Param("companyId") String companyId,
                                                                                  @Param("jobPositionId") String jobPositionId,
                                                                                  @Param("statusList") List<ApplicationStatus> applicationStatusList,
                                                                                  Pageable pageable);

    @Query("select a from ApplicationEntity a " +
            "where a.boothJobPosition.jobFairBooth.jobFair.company.id  = :companyId " +
            "and a.boothJobPosition.jobFairBooth.jobFair.id = :jobFairId and a.status in :statusList")
    Page<ApplicationEntity> findAllApplicationOfCompanyByJobFairIdAndStatusIn(@Param("companyId") String companyId,
                                                                              @Param("jobFairId") String jobFairId,
                                                                              @Param("statusList") List<ApplicationStatus> applicationStatusList,
                                                                              Pageable pageable);

    @Query("select a from ApplicationEntity a " +
            "where a.boothJobPosition.jobFairBooth.jobFair.company.id = :companyId and a.boothJobPosition.title like concat('%', :jobTitle, '%') " +
            "and a.boothJobPosition.jobFairBooth.jobFair.name like concat('%', :jobFairName, '%') and a.status in :statusList")
    Page<ApplicationEntity> findAllApplicationOfCompanyByJobPositionTitleLikeAndJobFairNameLikeAndStatusIn(@Param("companyId") String companyId,
                                                                                                           @Param("jobTitle") String jobTitle,
                                                                                                           @Param("jobFairName") String jobFairName,
                                                                                                           @Param("statusList") List<ApplicationStatus> applicationStatusList,
                                                                                                           Pageable pageable);


    @Query("select a from ApplicationEntity a where a.id = :applicationId and a.boothJobPosition.jobFairBooth.jobFair.company.id = :companyId")
    Optional<ApplicationEntity> findByIdAndCompanyId(@Param("applicationId") String applicationId,
                                                     @Param("companyId") String companyId);

    List<ApplicationEntity> findByCvIdAndBoothJobPositionIdAndStatusIn(String cv, String jobPositionId, List<ApplicationStatus> status);
}