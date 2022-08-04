package org.capstone.job_fair.services.impl.job_fair;

import com.amazonaws.util.json.Jackson;
import lombok.SneakyThrows;
import org.capstone.job_fair.constants.AWSConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.dynamoDB.NotificationMessageDTO;
import org.capstone.job_fair.models.entities.dynamoDB.JobFairVisitEntity;
import org.capstone.job_fair.models.entities.dynamoDB.JobhubConnectionsEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.JobFairBoothEntity;
import org.capstone.job_fair.models.enums.NotificationType;
import org.capstone.job_fair.repositories.job_fair.job_fair_booth.JobFairBoothRepository;
import org.capstone.job_fair.repositories.local_dynamo.JobFairVisitRepository;
import org.capstone.job_fair.repositories.local_dynamo.JobHubConnectionsRepository;
import org.capstone.job_fair.services.interfaces.job_fair.JobFairVisitService;
import org.capstone.job_fair.services.interfaces.notification.NotificationService;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Qualifier("LocalJobFairVisitService")
public class LocalJobFairVisitService implements JobFairVisitService {

    @Autowired
    @Qualifier("LocalNotificationService")
    private NotificationService notificationService;

    @Autowired
    private JobFairBoothRepository jobFairBoothRepository;

    @Autowired
    private JobHubConnectionsRepository jobHubConnectionsRepository;

    @Autowired
    private JobFairVisitRepository jobFairVisitRepository;

    private List<String> getConnectedUsers() {
        List<JobhubConnectionsEntity> scanResult = jobHubConnectionsRepository.findAll();
        List<String> userIds = scanResult.stream().map(JobhubConnectionsEntity::getUserId).collect(Collectors.toList());
        return userIds;

    }

    @SneakyThrows
    private void sendJobFairCountToConnectedUser(String jobFairId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("jobFairId", jobFairId);
        payload.put("count", getCurrentVisitOfJobFair(jobFairId));


        //get current connected user
        List<String> userIds = getConnectedUsers();

        NotificationMessageDTO notificationMessage = NotificationMessageDTO.builder()
                .title("Visit job fair")
                .message(Jackson.getObjectMapper().writeValueAsString(payload))
                .notificationType(NotificationType.VISIT_JOB_FAIR).build();


        notificationService.createNotification(notificationMessage, userIds);
    }

    @SneakyThrows
    private void sendBoothCountToConnectedUser(String jobFairId, String jobFairBoothId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("jobFairId", jobFairId);
        payload.put("jobFairBoothId", jobFairBoothId);
        payload.put("count", getCurrentVisitOfJobFairBooth(jobFairBoothId));

        //get current connected user
        List<String> userIds = getConnectedUsers();

        NotificationMessageDTO notificationMessage = NotificationMessageDTO.builder()
                .title("Visit job fair booth")
                .message(Jackson.getObjectMapper().writeValueAsString(payload))
                .notificationType(NotificationType.VISIT_JOB_FAIR_BOOTH).build();


        notificationService.createNotification(notificationMessage, userIds);
    }


    @Override
    @Transactional
    public void visitJobFair(String userId, String jobFairId) {
        JobFairVisitEntity entity = new JobFairVisitEntity();
        entity.setUserId(userId);
        entity.setJobFairId(jobFairId);
        jobFairVisitRepository.save(entity);
        sendJobFairCountToConnectedUser(jobFairId);
    }

    @Override
    @Transactional
    public void leaveJobFair(String userId, String jobFairId) {
        List<JobFairVisitEntity> jobFairVisits = jobFairVisitRepository.findByJobFairIdAndUserId(jobFairId, userId);
        jobFairVisitRepository.deleteAll(jobFairVisits);
        sendJobFairCountToConnectedUser(jobFairId);
    }

    @Override
    @SneakyThrows
    @Transactional
    public void visitBooth(String userId, String jobFairBoothId) {
        Thread.sleep(AWSConstant.DYNAMO_DELAY_TIME);
        //this is a shitty method this should be optimized

        Optional<JobFairBoothEntity> jobFairBoothOpt = jobFairBoothRepository.findById(jobFairBoothId);
        if (!jobFairBoothOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFairBooth.NOT_FOUND));
        }
        JobFairBoothEntity jobFairBooth = jobFairBoothOpt.get();
        JobFairVisitEntity entity = new JobFairVisitEntity();
        entity.setUserId(userId);
        entity.setJobFairId(jobFairBooth.getJobFair().getId());
        entity.setJobFairBoothId(jobFairBoothId);
        jobFairVisitRepository.save(entity);
        //send data
        sendBoothCountToConnectedUser(jobFairBooth.getJobFair().getId(), jobFairBoothId);

    }

    @Override
    @Transactional
    public void leaveBooth(String userId, String jobFairBoothId) {
        Optional<JobFairBoothEntity> jobFairBoothOpt = jobFairBoothRepository.findById(jobFairBoothId);
        if (!jobFairBoothOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFairBooth.NOT_FOUND));
        }
        JobFairBoothEntity jobFairBooth = jobFairBoothOpt.get();

        List<JobFairVisitEntity> queryResult = jobFairVisitRepository.findByJobFairBoothIdAndUserId(jobFairBoothId, userId);
        //delete visitation
        jobFairVisitRepository.deleteAll(queryResult);
        //send data
        sendBoothCountToConnectedUser(jobFairBooth.getJobFair().getId(), jobFairBoothId);

    }

    @Override
    public int getCurrentVisitOfJobFair(String jobFairId) {
        return (int) jobFairVisitRepository.countByJobFairId(jobFairId);
    }

    @Override
    public int getCurrentVisitOfJobFairBooth(String jobFairBootId) {
        return (int) jobFairVisitRepository.countByJobFairBoothId(jobFairBootId);
    }
}
