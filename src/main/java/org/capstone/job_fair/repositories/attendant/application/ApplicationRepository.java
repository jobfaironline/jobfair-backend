package org.capstone.job_fair.repositories.attendant.application;

import org.capstone.job_fair.models.entities.attendant.application.ApplicationEntity;
import org.capstone.job_fair.models.enums.ApplicationStatus;
import org.capstone.job_fair.models.statuses.InterviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, String> {

    @Query("select a from ApplicationEntity a " +
            "where a.attendant.accountId = :accountId and " +
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

    List<ApplicationEntity> findByOriginCvIdAndBoothJobPositionIdAndStatusIn(String cv, String jobPositionId, List<ApplicationStatus> status);

    Optional<ApplicationEntity> findByIdAndAttendantAccountId(String applicationId, String attendantId);

    //find application that has interview schedule that fit  entirely inside the time span
    @Query("select a from ApplicationEntity a where a.interviewer is not null and a.interviewer.accountId = :employeeId and (a.beginTime >= :beginTime) and (a.endTime <= :endTime)")
    List<ApplicationEntity> findWholeByInterviewerAndInTimeRange(@Param("employeeId") String employeeId,
                                                                 @Param("beginTime") Long beginTime,
                                                                 @Param("endTime") Long endTime);

    //find application that has interview schedule that fit entirely inside the time span
    @Query("select a from ApplicationEntity a where a.attendant.accountId = :attendantId and (a.beginTime >= :beginTime) and (a.endTime <= :endTime)")
    List<ApplicationEntity> findWholeByAttendantAndInTimeRange(@Param("attendantId") String attendantId,
                                                               @Param("beginTime") Long beginTime,
                                                               @Param("endTime") Long endTime);

    //find application that has interview schedule that fit inside the time span (no need to be entirely fit inside this time span)
    @Query("select a from ApplicationEntity a where a.interviewer is not null and a.interviewer.accountId = :employeeId and ((a.beginTime >= :beginTime) and (a.endTime <= :endTime)) or (a.beginTime < :beginTime and a.endTime > :beginTime) or (a.beginTime < :endTime and a.endTime > :endTime)")
    List<ApplicationEntity> findByInterviewerAndInTimeRange(@Param("employeeId") String employeeId,
                                                            @Param("beginTime") Long beginTime,
                                                            @Param("endTime") Long endTime);

    //find application that has interview schedule that fit inside the time span (no need to be entirely fit inside this time span)
    @Query("select a from ApplicationEntity a where a.attendant.accountId = :attendantId and ((a.beginTime >= :beginTime) and (a.endTime <= :endTime)) or (a.beginTime < :beginTime and a.endTime > :beginTime) or (a.beginTime < :endTime and a.endTime > :endTime)")
    List<ApplicationEntity> findByAttendantAndInTimeRange(@Param("attendantId") String attendantId,
                                                          @Param("beginTime") Long beginTime,
                                                          @Param("endTime") Long endTime);

    @Query("select a from ApplicationEntity a where a.waitingRoomId = :waitingRoomId and a.interviewStatus = :interviewStatus and (a.endTime <= :endTime or a.beginTime >= :beginTime) ")
    List<ApplicationEntity> findWaitingAttendant(
            @Param("waitingRoomId") String waitingRoomId,
            @Param("interviewStatus") InterviewStatus status,
            @Param("endTime") long endTime,
            @Param("beginTime") long beginTime);

    @Query("select a from ApplicationEntity a where a.waitingRoomId = :waitingRoomId and (a.endTime <= :endTime or a.beginTime >= :beginTime) and a.interviewer.accountId = :employeeId")
    List<ApplicationEntity> findWaitingAttendantByEmployeeId(
            @Param("waitingRoomId") String waitingRoomId,
            @Param("beginTime") long beginTime,
            @Param("endTime") long endTime,
            @Param("employeeId") String employeeId);

    Optional<ApplicationEntity> findByWaitingRoomId(String waitingRoomId);
}