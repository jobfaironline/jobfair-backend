package org.capstone.job_fair.services.interfaces.attendant.application;

import org.capstone.job_fair.models.dtos.attendant.application.ApplicationDTO;
import org.capstone.job_fair.models.entities.attendant.application.ApplicationEntity;
import org.capstone.job_fair.models.enums.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ApplicationService {
    ApplicationDTO createNewApplication(ApplicationDTO dto);

    Page<ApplicationEntity> getApplicationOfCompanyByJobPositionIdAndStatus(String companyId, String jobPositionId, List<ApplicationStatus> statusList, int pageSize, int offset, String sortBy, Sort.Direction direction);

    Page<ApplicationEntity> getApplicationOfCompanyByJobFairIdAndStatus(String companyId, String jobFairId, List<ApplicationStatus> statusList, int pageSize, int offset, String sortBy, Sort.Direction direction);

    Page<ApplicationEntity> getApplicationOfCompanyByJobFairNameAndJobPositionNameAndAttendantNameAndStatus(String companyId, String jobFairName, String jobPositionName, String attendantName, List<ApplicationStatus> statusList, int pageSize, int offset, String sortBy, Sort.Direction direction);

    Optional<ApplicationEntity> getApplicationWithGeneralDataByIdOfCompany(String companyId, String applicationId);

    ApplicationDTO evaluateApplication(ApplicationDTO dto, String employeeId);

    Page<ApplicationEntity> getAllApplicationsOfAttendantByCriteria(String userId, String jobFairName, String jobPositionName, List<ApplicationStatus> statusList, Long fromTime, Long toTime, int offset, int pageSize, String sortBy, Sort.Direction direction);

    ApplicationDTO submitApplication(String applicationId, String userId);

    Optional<ApplicationDTO> getApplicationByIdForAttendant(String applicationId, String userId);

    Optional<ApplicationDTO> getApplicationByIdForCompanyEmployee(String applicationId, String userId);

    String exportApplicationByJobFair(String companyId, String jobFairId);

}
