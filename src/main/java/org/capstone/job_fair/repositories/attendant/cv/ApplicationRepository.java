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
            "a.registrationJobPosition.title like concat('%',:jobPositionName,'%') and " +
            "a.registrationJobPosition.companyRegistration.jobFairEntity.name like concat('%',:jobFairName,'%')")
    Page<ApplicationEntity> findAllApplicationOfAttendantByCriteria(@Param("accountId") String acocuntId,
                                                                    @Param("fromDate") Long fromDate,
                                                                    @Param("toDate") Long toDate,
                                                                    @Param("statusList") List<ApplicationStatus> statusList,
                                                                    @Param("jobPositionName") String jobPositionName,
                                                                    @Param("jobFairName") String jobFairName,
                                                                    Pageable pageable);

    @Query("select a from ApplicationEntity a " +
            "where a.registrationJobPosition.companyRegistration.companyId = :companyId " +
            "and a.registrationJobPosition.id = :jobPositionId and a.status in :statusList")
    Page<ApplicationEntity> findAllApplicationOfCompanyByJobPositionIdAndStatusIn(@Param("companyId") String companyId,
                                                                                  @Param("jobPositionId") String jobPositionId,
                                                                                  @Param("statusList") List<ApplicationStatus> applicationStatusList,
                                                                                  Pageable pageable);

    @Query("select a from ApplicationEntity a " +
            "where a.registrationJobPosition.companyRegistration.companyId = :companyId " +
            "and a.registrationJobPosition.companyRegistration.jobFairId = :jobFairId and a.status in :statusList")
    Page<ApplicationEntity> findAllApplicationOfCompanyByJobFairIdAndStatusIn(@Param("companyId") String companyId,
                                                                              @Param("jobFairId") String jobFairId,
                                                                              @Param("statusList") List<ApplicationStatus> applicationStatusList,
                                                                              Pageable pageable);

    @Query("select a from ApplicationEntity a " +
            "where a.registrationJobPosition.companyRegistration.companyId = :companyId and a.registrationJobPosition.title like concat('%', :jobTitle, '%') " +
            "and a.registrationJobPosition.companyRegistration.jobFairEntity.name like concat('%', :jobFairName, '%') and a.status in :statusList")
    Page<ApplicationEntity> findAllApplicationOfCompanyByJobPositionTitleLikeAndJobFairNameLikeAndStatusIn(@Param("companyId") String companyId,
                                                                                                           @Param("jobTitle") String jobTitle,
                                                                                                           @Param("jobFairName") String jobFairName,
                                                                                                           @Param("statusList") List<ApplicationStatus> applicationStatusList,
                                                                                                           Pageable pageable);

    Optional<ApplicationEntity> findByIdAndRegistrationJobPositionCompanyRegistrationCompanyId(String applicationId, String companyId);
}