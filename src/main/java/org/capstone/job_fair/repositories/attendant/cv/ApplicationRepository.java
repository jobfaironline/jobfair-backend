package org.capstone.job_fair.repositories.attendant.cv;

import org.capstone.job_fair.models.entities.attendant.cv.ApplicationEntity;
import org.capstone.job_fair.models.enums.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, String> {

    Page<ApplicationEntity> findAllByCreateDateBetweenAndStatusAndCvAttendantAccountId(long fromDate, long toDate, Application status, String id, Pageable pageable);

    @Query("select a from ApplicationEntity a " +
            "where a.registrationJobPosition.companyRegistration.companyId = ?1 " +
            "and a.registrationJobPosition.id = ?2 and a.status in ?3")
    Page<ApplicationEntity> findAllApplicationOfCompanyByJobPositionIdAndStatusIn(String companyId, String jobPositionId, List<Application> applicationList, Pageable pageable);

    @Query("select a from ApplicationEntity a " +
            "where a.registrationJobPosition.companyRegistration.companyId = ?1 " +
            "and a.registrationJobPosition.companyRegistration.jobFairId = ?2 and a.status in ?3")
    Page<ApplicationEntity> findAllApplicationOfCompanyByJobFairIdAndStatusIn(String companyId, String jobFairId, List<Application> applicationList, Pageable pageable);

    @Query("select a from ApplicationEntity a " +
            "where a.registrationJobPosition.companyRegistration.companyId = ?1 and a.registrationJobPosition.title like concat('%', ?2, '%') " +
            "and a.registrationJobPosition.companyRegistration.jobFairEntity.name like concat('%', ?3, '%') and a.status in ?4")
    Page<ApplicationEntity> findAllApplicationOfCompanyByJobPositionTitleLikeAndJobFairNameLikeAndStatusIn(String companyId, String jobTitle, String jobFairName, List<Application> applicationList, Pageable pageable);

}