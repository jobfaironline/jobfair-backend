package org.capstone.job_fair.services.impl.job_fair;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.util.json.Jackson;
import lombok.SneakyThrows;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.constants.ScheduleConstant;
import org.capstone.job_fair.models.dtos.dynamoDB.NotificationMessageDTO;
import org.capstone.job_fair.models.dtos.job_fair.InterviewRequestChangeDTO;
import org.capstone.job_fair.models.dtos.job_fair.InterviewScheduleDTO;
import org.capstone.job_fair.models.entities.attendant.application.ApplicationEntity;
import org.capstone.job_fair.models.entities.dynamoDB.WaitingRoomVisitEntity;
import org.capstone.job_fair.models.entities.job_fair.InterviewRequestChangeEntity;
import org.capstone.job_fair.models.enums.InterviewRequestChangeStatus;
import org.capstone.job_fair.models.enums.NotificationType;
import org.capstone.job_fair.models.statuses.InterviewStatus;
import org.capstone.job_fair.repositories.attendant.application.ApplicationRepository;
import org.capstone.job_fair.repositories.job_fair.InterviewRequestChangeRepository;
import org.capstone.job_fair.services.interfaces.job_fair.InterviewService;
import org.capstone.job_fair.services.interfaces.notification.NotificationService;
import org.capstone.job_fair.services.mappers.job_fair.InterviewRequestChangeMapper;
import org.capstone.job_fair.services.mappers.job_fair.InterviewScheduleMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class InterviewServiceImpl implements InterviewService {
    @Autowired
    private InterviewScheduleMapper interviewScheduleMapper;

    @Autowired
    private InterviewRequestChangeMapper interviewRequestChangeMapper;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private InterviewRequestChangeRepository interviewRequestChangeRepository;

    @Autowired
    private AmazonDynamoDB dynamoDBClient;

    @Autowired
    private NotificationService notificationService;

    @Override
    public List<InterviewScheduleDTO> getInterviewScheduleForCompanyEmployee(String employeeId, Long beginTime, Long endTime) {
        return applicationRepository.findWholeByInterviewerAndInTimeRange(employeeId, beginTime, endTime)
                .stream()
                .map(interviewScheduleMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InterviewScheduleDTO> getInterviewScheduleForAttendant(String attendantId, Long beginTime, Long endTime) {
        return applicationRepository.findWholeByAttendantAndInTimeRange(attendantId, beginTime, endTime)
                .stream()
                .map(interviewScheduleMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InterviewRequestChangeDTO requestChangeSchedule(String applicationId, String userId, boolean isAttendant, String requestMessage, Long beginTime, Long endTime) {
        Optional<ApplicationEntity> applicationOpt = applicationRepository.findById(applicationId);
        //check existed application
        if (!applicationOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.APPLICATION_NOT_FOUND));
        }
        ApplicationEntity application = applicationOpt.get();

        //check ìf this application has schedule interview
        if (application.getInterviewer() == null) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.InterviewSchedule.NOT_FOUND));
        }
        if (application.getInterviewStatus() != InterviewStatus.NOT_YET) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.InterviewSchedule.CANNOT_EDIT));
        }
        //check if allow request change
        long now = new Date().getTime();
        if (application.getEndTime() > now + ScheduleConstant.BUFFER_CHANGE_INTERVIEW_SCHEDULE) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.InterviewSchedule.CANNOT_EDIT));
        }
        //check if endTime inside job fair public time
        if (endTime > application.getBoothJobPosition().getJobFairBooth().getJobFair().getPublicEndTime()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.InterviewSchedule.INVALID_REQUEST_SCHEDULE));
        }
        //check if beginTime and endTime is not in the past
        if (beginTime < now || endTime < now) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.InterviewSchedule.INVALID_REQUEST_SCHEDULE));
        }
        //check if schedule conflict
        List<ApplicationEntity> availableApplication;
        if (isAttendant) {
            availableApplication = applicationRepository.findByAttendantAndInTimeRange(userId, beginTime, endTime);
        } else {
            availableApplication = applicationRepository.findByInterviewerAndInTimeRange(userId, beginTime, endTime);
        }
        if (!availableApplication.isEmpty()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.InterviewSchedule.SCHEDULE_CONFLICT));
        }
        application.setInterviewStatus(InterviewStatus.REQUEST_CHANGE);
        InterviewRequestChangeEntity requestChange = new InterviewRequestChangeEntity();
        requestChange.setApplication(application);
        requestChange.setMessage(requestMessage);
        requestChange.setBeginTime(beginTime);
        requestChange.setEndTime(endTime);
        requestChange.setStatus(InterviewRequestChangeStatus.PENDING);
        requestChange.setCreateTime(now);
        requestChange = interviewRequestChangeRepository.save(requestChange);
        applicationRepository.save(application);
        return interviewRequestChangeMapper.toDTO(requestChange);

    }

    @Override
    @Transactional
    public InterviewRequestChangeDTO evaluateRequestChange(String requestChangeId, InterviewRequestChangeStatus status, String userId) {
        Optional<InterviewRequestChangeEntity> requestOpt = interviewRequestChangeRepository.findById(requestChangeId);
        if (!requestOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.InterviewRequestChange.NOT_FOUND));
        }
        InterviewRequestChangeEntity requestChange = requestOpt.get();
        if (status != InterviewRequestChangeStatus.REJECT && status != InterviewRequestChangeStatus.APPROVE) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.InterviewRequestChange.INVALID_STATUS));
        }
        if (requestChange.getStatus() != InterviewRequestChangeStatus.PENDING) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.InterviewRequestChange.CANNOT_EDIT));
        }
        if (!requestChange.getApplication().getInterviewer().getAccountId().equals(userId)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.InterviewRequestChange.NOT_FOUND));
        }

        ApplicationEntity application = requestChange.getApplication();
        application.setInterviewStatus(InterviewStatus.NOT_YET);
        requestChange.setStatus(status);
        requestChange = interviewRequestChangeRepository.save(requestChange);
        applicationRepository.save(application);
        return interviewRequestChangeMapper.toDTO(requestChange);
    }

    private void checkApplicationValid(String applicationId, String userId, boolean isAttendant) {
        Optional<ApplicationEntity> applicationOpt = applicationRepository.findById(applicationId);
        if (!applicationOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.APPLICATION_NOT_FOUND));
        }
        ApplicationEntity application = applicationOpt.get();
        if (isAttendant) {
            if (!application.getAttendant().getAccountId().equals(userId)) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.APPLICATION_NOT_FOUND));
            }
        } else {
            if (!application.getInterviewer().getAccountId().equals(userId)) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.APPLICATION_NOT_FOUND));
            }
        }
    }

    @Override
    public Optional<InterviewRequestChangeDTO> getLatestRequestChangeByApplication(String applicationId, String userId, boolean isAttendant) {
        checkApplicationValid(applicationId, userId, isAttendant);
        Optional<InterviewRequestChangeEntity> requestChangeOpt = interviewRequestChangeRepository.findTopByApplicationIdOrderByCreateTimeDesc(applicationId);
        return requestChangeOpt.map(interviewRequestChangeMapper::toDTO);
    }

    @Override
    public List<InterviewRequestChangeDTO> getRequestChangesByApplication(String applicationId, String userId, boolean isAttendant) {
        checkApplicationValid(applicationId, userId, isAttendant);
        List<InterviewRequestChangeEntity> result = interviewRequestChangeRepository.findByApplicationIdOrderByCreateTimeDesc(applicationId);
        return result.stream().map(interviewRequestChangeMapper::toDTO).collect(Collectors.toList());
    }

    @SneakyThrows
    private void sendWaitingCountToConnectedUser(String channelId, String userId, boolean isLeaveRoom) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userId);

        //get current connected user
        List<String> userIds = getConnectedUserIds(channelId);
        payload.put("connectedUserIds", userIds);
        payload.put("isLeaveRoom", isLeaveRoom);

        NotificationMessageDTO notificationMessage = NotificationMessageDTO.builder()
                .title("Waiting room - Change in user id")
                .message(Jackson.getObjectMapper().writeValueAsString(payload))
                .notificationType(NotificationType.WAITING_ROOM).build();

        notificationService.createNotification(notificationMessage, userIds);

    }

    @Override
    public void visitWaitingRoom(String channelId, String userId, boolean isAttendant) {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient);
        WaitingRoomVisitEntity entity = new WaitingRoomVisitEntity();
        entity.setUserId(userId);
        entity.setAttendant(isAttendant);
        entity.setChannelId(channelId);
        dynamoDBMapper.save(entity);
        this.sendWaitingCountToConnectedUser(channelId, userId, false);
    }

    @Override
    public void leaveWaitingRoom(String channelId, String userId, boolean isAttendant) {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient);

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":channelId", new AttributeValue().withS(channelId));
        eav.put(":userId", new AttributeValue().withS(userId));

        DynamoDBQueryExpression<WaitingRoomVisitEntity> queryExpression = new DynamoDBQueryExpression<WaitingRoomVisitEntity>()
                .withKeyConditionExpression("channelId = :channelId AND userId = :userId")
                .withExpressionAttributeValues(eav);

        List<WaitingRoomVisitEntity> queryResult = dynamoDBMapper.query(WaitingRoomVisitEntity.class, queryExpression);
        dynamoDBMapper.batchDelete(queryResult);
        this.sendWaitingCountToConnectedUser(channelId, userId, true);
    }

    @Override
    public List<String> getConnectedUserIds(String channelId) {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient);
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":channelId", new AttributeValue().withS(channelId));

        DynamoDBQueryExpression<WaitingRoomVisitEntity> queryExpression = new DynamoDBQueryExpression<WaitingRoomVisitEntity>()
                .withKeyConditionExpression("channelId = :channelId")
                .withExpressionAttributeValues(eav);


        List<WaitingRoomVisitEntity> scanResult = dynamoDBMapper.query(WaitingRoomVisitEntity.class, queryExpression);
        List<String> userIds = scanResult.stream().map(WaitingRoomVisitEntity::getUserId).collect(Collectors.toList());
        return userIds;
    }

    @Override
    public void requestInterviewAttendant(String attendantId, String waitingRoomId) {
        List<String> connectedUserIds = this.getConnectedUserIds(waitingRoomId);
        if (!connectedUserIds.contains(attendantId)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Interview.ATTENDANT_NOT_FOUND));
        }

        NotificationMessageDTO notificationMessage = NotificationMessageDTO.builder()
                .title("Review room - Invite to interview")
                .message("Go to room please")
                .notificationType(NotificationType.INTERVIEW_ROOM).build();

        notificationService.createNotification(notificationMessage, attendantId);

    }
}
