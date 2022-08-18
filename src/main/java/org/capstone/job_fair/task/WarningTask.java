package org.capstone.job_fair.task;

import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import org.capstone.job_fair.models.dtos.dynamoDB.NotificationMessageDTO;
import org.capstone.job_fair.models.dtos.job_fair.JobFairProgressDTO;
import org.capstone.job_fair.models.entities.attendant.application.ApplicationEntity;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.AssignmentEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.JobFairBoothEntity;
import org.capstone.job_fair.models.enums.NotificationAction;
import org.capstone.job_fair.models.enums.NotificationType;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.models.statuses.InterviewStatus;
import org.capstone.job_fair.models.statuses.JobFairPlanStatus;
import org.capstone.job_fair.repositories.attendant.application.ApplicationRepository;
import org.capstone.job_fair.repositories.company.CompanyEmployeeRepository;
import org.capstone.job_fair.repositories.job_fair.JobFairRepository;
import org.capstone.job_fair.repositories.job_fair.job_fair_booth.AssignmentRepository;
import org.capstone.job_fair.services.interfaces.job_fair.JobFairService;
import org.capstone.job_fair.services.interfaces.notification.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class WarningTask {

    private static final Logger log = LoggerFactory.getLogger(WarningTask.class);
    @Autowired
    private JobFairRepository jobFairRepository;

    @Autowired
    private JobFairService jobFairService;

    @Autowired
    private CompanyEmployeeRepository companyEmployeeRepository;

    @Autowired
    @Qualifier("LocalNotificationService")
    private NotificationService notificationService;

    @Autowired
    private Clock clock;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Value("${warning.interview.ahead}")
    private long interviewAhead;

    @Scheduled(fixedDelayString = "${warning.task.millis}")
    public void warningTaskForJobFair() {
        log.info("Send warning task for job fair");
        List<JobFairEntity> jobFairEntities = jobFairRepository.findByStatus(JobFairPlanStatus.PUBLISH);
        jobFairEntities.forEach(jobFair -> {
            List<CompanyEmployeeEntity> managers = companyEmployeeRepository.findByCompanyIdAndAccountRoleId(jobFair.getCompany().getId(), Role.COMPANY_MANAGER.ordinal());
            long decorateEndTime = jobFair.getDecorateEndTime();
            long now = clock.millis();
            if (now > decorateEndTime) return;

            JobFairProgressDTO progressDTO = jobFairService.getJobFairProgress(jobFair.getId());
            if (progressDTO.getOverallProgress() == 1.0) return;
            //send notification for manager
            Map<String, String> managerBody = new HashMap<>();
            managerBody.put("jobFairId", jobFair.getId());
            managerBody.put("endTime", "" + decorateEndTime);
            try {
                NotificationMessageDTO notificationMessage = NotificationMessageDTO.builder()
                        .title(NotificationAction.WARNING_TASK_MANAGER.toString())
                        .message(Jackson.getObjectMapper().writeValueAsString(managerBody))
                        .notificationType(NotificationType.NOTI).build();
                List<String> userIds = managers.stream().map(CompanyEmployeeEntity::getAccountId).collect(Collectors.toList());
                userIds = new ArrayList<>(new HashSet<>(userIds));
                notificationService.createNotification(notificationMessage, userIds);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
            //send notification for booth
            progressDTO.getBooths().forEach(booth -> {
                if (booth.getProgress() == 1.0) return;
                //send notification for supervisor
                if (!booth.getSupervisor().isBoothProfile()) {
                    try {
                        Map<String, String> supervisorBody = new HashMap<>();
                        supervisorBody.put("assignmentId", booth.getSupervisor().getAssignmentId());
                        supervisorBody.put("endTime", "" + decorateEndTime);
                        NotificationMessageDTO notificationMessage = NotificationMessageDTO.builder()
                                .title(NotificationAction.WARNING_TASK_SUPERVISOR_PROFILE.toString())
                                .message(Jackson.getObjectMapper().writeValueAsString(supervisorBody))
                                .notificationType(NotificationType.NOTI).build();
                        notificationService.createNotification(notificationMessage, booth.getSupervisor().getId());
                    } catch (JsonProcessingException e) {
                        log.error(e.getMessage());
                    }
                }
                if (!booth.getSupervisor().isAssignTask()) {
                    try {
                        Map<String, String> supervisorBody = new HashMap<>();
                        supervisorBody.put("jobFairBoothId", booth.getId());
                        supervisorBody.put("endTime", "" + decorateEndTime);
                        NotificationMessageDTO notificationMessage = NotificationMessageDTO.builder()
                                .title(NotificationAction.WARNING_TASK_SUPERVISOR_ASSIGN.toString())
                                .message(Jackson.getObjectMapper().writeValueAsString(supervisorBody))
                                .notificationType(NotificationType.NOTI).build();
                        notificationService.createNotification(notificationMessage, booth.getSupervisor().getId());
                    } catch (JsonProcessingException e) {
                        log.error(e.getMessage());
                    }
                }
                //send notification for decorator
                if (!booth.getDecorator().isDecorate()) {
                    try {
                        Map<String, String> decoratorBody = new HashMap<>();
                        decoratorBody.put("jobFairBoothId", booth.getId());
                        decoratorBody.put("jobFairId", jobFair.getId());
                        decoratorBody.put("endTime", "" + decorateEndTime);
                        NotificationMessageDTO notificationMessage = NotificationMessageDTO.builder()
                                .title(NotificationAction.WARNING_TASK_DECORATOR.toString())
                                .message(Jackson.getObjectMapper().writeValueAsString(decoratorBody))
                                .notificationType(NotificationType.NOTI).build();
                        notificationService.createNotification(notificationMessage, booth.getDecorator().getId());
                    } catch (JsonProcessingException e) {
                        log.error(e.getMessage());
                    }
                }
            });
        });
    }

    @Scheduled(fixedDelayString = "${warning.interview.millis}")
    @SneakyThrows
    public void warningInterview(){
        log.info("Send warning interview for job fair");
        Page<JobFairEntity> jobFairs = jobFairRepository.findInProgressJobFair("%%", clock.millis(), Pageable.unpaged());
        for (JobFairEntity jobFair : jobFairs.toList()){
            List<ApplicationEntity> applications = applicationRepository.findByInJobFairIdAndInterviewStatus(jobFair.getId(), InterviewStatus.NOT_YET);
            for (ApplicationEntity application: applications){
                if (application.getBeginTime() - clock.millis() < interviewAhead ){
                    Map<String, String> managerBody = new HashMap<>();
                    managerBody.put("jobFairId", jobFair.getId());
                    NotificationMessageDTO notificationMessage = NotificationMessageDTO.builder()
                            .title(NotificationAction.WARNING_INTERVIEW.toString())
                            .message(Jackson.getObjectMapper().writeValueAsString(managerBody))
                            .notificationType(NotificationType.NOTI).build();
                    List<String> userIds = Arrays.asList(application.getInterviewer().getAccountId(), application.getAttendant().getAccountId());
                    userIds = new ArrayList<>(new HashSet<>(userIds));
                    notificationService.createNotification(notificationMessage, userIds);
                }
            }
        }


    }
}
