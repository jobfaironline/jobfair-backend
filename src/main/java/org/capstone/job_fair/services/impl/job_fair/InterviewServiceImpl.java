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
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.capstone.job_fair.models.entities.dynamoDB.WaitingRoomVisitEntity;
import org.capstone.job_fair.models.entities.job_fair.InterviewRequestChangeEntity;
import org.capstone.job_fair.models.enums.ApplicationStatus;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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
    private void sendWaitingCountToConnectedUser(String channelId, String userId, String interviewerId, boolean isLeaveRoom) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userId);

        //get current connected user
        List<String> userIds = getConnectedUserIds(channelId);


        userIds.add(interviewerId);

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
        Optional<ApplicationEntity> applicationOpt = applicationRepository.findByWaitingRoomId(channelId);
        if (!applicationOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.APPLICATION_NOT_FOUND));
        }
        ApplicationEntity application = applicationOpt.get();

        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient);
        WaitingRoomVisitEntity entity = new WaitingRoomVisitEntity();
        entity.setUserId(userId);
        entity.setAttendant(isAttendant);
        entity.setChannelId(channelId);
        dynamoDBMapper.save(entity);


        this.sendWaitingCountToConnectedUser(channelId, userId, application.getInterviewer().getAccountId(), false);
    }

    @Override
    public void leaveWaitingRoom(String channelId, String userId, boolean isAttendant) {
        Optional<ApplicationEntity> applicationOpt = applicationRepository.findByWaitingRoomId(channelId);
        if (!applicationOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.APPLICATION_NOT_FOUND));
        }
        ApplicationEntity application = applicationOpt.get();


        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient);

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":channelId", new AttributeValue().withS(channelId));
        eav.put(":userId", new AttributeValue().withS(userId));

        DynamoDBQueryExpression<WaitingRoomVisitEntity> queryExpression = new DynamoDBQueryExpression<WaitingRoomVisitEntity>()
                .withKeyConditionExpression("channelId = :channelId AND userId = :userId")
                .withExpressionAttributeValues(eav);

        List<WaitingRoomVisitEntity> queryResult = dynamoDBMapper.query(WaitingRoomVisitEntity.class, queryExpression);
        dynamoDBMapper.batchDelete(queryResult);
        this.sendWaitingCountToConnectedUser(channelId, userId, application.getInterviewer().getAccountId(), true);
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
    @SneakyThrows
    public void askAttendantJoinInterviewRoom(String attendantId, String interviewRoomId) {
        List<String> connectedUserIds = this.getConnectedUserIds(interviewRoomId);
        if (!connectedUserIds.contains(attendantId)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Interview.ATTENDANT_NOT_FOUND));
        }

        HashMap<String, String> payload = new HashMap<>();
        payload.put("interviewRoomId", interviewRoomId);

        NotificationMessageDTO notificationMessage = NotificationMessageDTO.builder()
                .title("Review room - Invite to interview")
                .message(Jackson.getObjectMapper().writeValueAsString(payload))
                .notificationType(NotificationType.INTERVIEW_ROOM).build();

        notificationService.createNotification(notificationMessage, attendantId);

    }

    @Override
    public int getAttendantTurnInWaitingRoom(String attendantId, String waitingRoomId) {
        long now = new Date().getTime();
        LocalDate localDate = LocalDate.now();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);
        long endTime = endOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        List<ApplicationEntity> applicationList = applicationRepository.findWaitingAttendant(waitingRoomId, InterviewStatus.INTERVIEWING, now, endTime);
        return applicationList.size();
    }

    @Override
    public List<InterviewScheduleDTO> getInterviewScheduleInWaitingRoom(String employeeId, String waitingRoomId) {
        LocalDate localDate = LocalDate.now();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);
        LocalDateTime startOfDay = localDate.atTime(LocalTime.MIN);
        long endTime = endOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long beginTime = startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        List<ApplicationEntity> applicationList = applicationRepository.findWaitingAttendantByEmployeeId(waitingRoomId, beginTime, endTime, employeeId);
        return applicationList.stream().map(interviewScheduleMapper::toDTO).collect(Collectors.toList());

    }


    private ApplicationEntity getValidApplicationEntity(String attendantId, String interviewRoomId, String reviewerId) {
        Optional<ApplicationEntity> applicationOpt = applicationRepository.findByInterviewRoomIdAndAttendantAccountId(interviewRoomId, attendantId);
        if (!applicationOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.APPLICATION_NOT_FOUND));
        }
        ApplicationEntity application = applicationOpt.get();
        if (!application.getInterviewer().getAccountId().equals(reviewerId)
        ) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Interview.REVIEWER_NOT_FOUND));
        }
        if (application.getInterviewStatus() == null || application.getInterviewStatus() != InterviewStatus.INTERVIEWING) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Interview.INVALID_STATUS));
        }
        return application;
    }

    @Override
    @Transactional
    public void finishInterview(String attendantId, String interviewRoomId, String reviewerId) {
        ApplicationEntity application = getValidApplicationEntity(attendantId, interviewRoomId, reviewerId);
        application.setInterviewStatus(InterviewStatus.DONE);
        applicationRepository.save(application);
    }

    @Override
    @Transactional
    public void startInterview(String attendantId, String interviewRoomId, String reviewerId) {
        ApplicationEntity application = getValidApplicationEntity(attendantId, interviewRoomId, reviewerId);
        application.setInterviewStatus(InterviewStatus.INTERVIEWING);
        applicationRepository.save(application);
    }


    @Override
    public InterviewScheduleDTO scheduleInterview(String applicationId, String interviewerId) {
        Optional<ApplicationEntity> applicationOpt = applicationRepository.findById(applicationId);
        if (!applicationOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.APPLICATION_NOT_FOUND));
        }
        ApplicationEntity application = applicationOpt.get();
        if (application.getStatus() != ApplicationStatus.APPROVE) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.InterviewSchedule.INVALID_APPLICATION_STATUS));
        }
        if (application.getInterviewStatus() != null) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.InterviewSchedule.ALREADY_SCHEDULE_INTERVIEW));
        }
        LocalDate localDate = LocalDate.now();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);
        LocalDateTime startOfDay = localDate.atTime(LocalTime.MIN);
        long endTime = endOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long beginTime = startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        List<ApplicationEntity> scheduleList = applicationRepository.findWholeByInterviewerAndInTimeRange(interviewerId, beginTime, endTime);
        int lastIndex = scheduleList.size() - 1;

        //TODO: get this from DB
        long interviewLength = 45 * 60 * 1000L;
        long bufferTime = 15 * 60 * 1000L;
        LocalDateTime sevenHour = startOfDay.plusHours(19);
        long endShiftTime = sevenHour.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long nextShiftTime = endShiftTime;
        //END TODO

        if (lastIndex > 0 && scheduleList.get(lastIndex).getEndTime() + interviewLength + bufferTime > endShiftTime) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.InterviewSchedule.MAXIMUM_SCHEDULE_ALLOW));
        }


        if (lastIndex > 0) {
            application.setBeginTime(scheduleList.get(lastIndex).getEndTime() + bufferTime);
        } else {
            application.setBeginTime(nextShiftTime);
        }

        application.setInterviewName("Interview with " + application.getAttendant().getAccount().getFullname());
        application.setInterviewDescription("Interview with " + application.getAttendant().getAccount().getFullname());
        application.setEndTime(application.getBeginTime() + interviewLength);
        application.setInterviewStatus(InterviewStatus.INTERVIEWING);
        CompanyEmployeeEntity companyEmployee = new CompanyEmployeeEntity();
        companyEmployee.setAccountId(interviewerId);
        application.setInterviewer(companyEmployee);
        String waitingRoomId = ScheduleConstant.WAITING_ROOM_PREFIX + UUID.randomUUID().toString();
        String interviewRoomId = ScheduleConstant.INTERVIEW_ROOM_PREFIX + UUID.randomUUID().toString();
        application.setWaitingRoomId(waitingRoomId);
        application.setInterviewRoomId(interviewRoomId);
        applicationRepository.save(application);
        return interviewScheduleMapper.toDTO(application);
    }

    @Override
    public Optional<InterviewScheduleDTO> getScheduleById(String id) {
        return applicationRepository.findById(id).map(interviewScheduleMapper::toDTO);
    }


    @Override
    public List<InterviewScheduleDTO> getScheduleByInterviewRoomId(String interviewRoomId) {
        return applicationRepository.findByInterviewRoomId(interviewRoomId).stream().map(interviewScheduleMapper::toDTO).collect(Collectors.toList());
    }

}
