package org.capstone.job_fair.services.impl.job_fair;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.util.json.Jackson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.constants.ScheduleConstant;
import org.capstone.job_fair.models.dtos.attendant.application.ApplicationDTO;
import org.capstone.job_fair.models.dtos.dynamoDB.NotificationMessageDTO;
import org.capstone.job_fair.models.dtos.job_fair.InterviewRequestChangeDTO;
import org.capstone.job_fair.models.dtos.job_fair.InterviewScheduleDTO;
import org.capstone.job_fair.models.entities.attendant.application.ApplicationEntity;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.capstone.job_fair.models.entities.dynamoDB.WaitingRoomVisitEntity;
import org.capstone.job_fair.models.entities.job_fair.InterviewRequestChangeEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.AssignmentEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.JobFairBoothEntity;
import org.capstone.job_fair.models.enums.ApplicationStatus;
import org.capstone.job_fair.models.enums.AssignmentType;
import org.capstone.job_fair.models.enums.InterviewRequestChangeStatus;
import org.capstone.job_fair.models.enums.NotificationType;
import org.capstone.job_fair.models.statuses.InterviewStatus;
import org.capstone.job_fair.repositories.attendant.application.ApplicationRepository;
import org.capstone.job_fair.repositories.job_fair.InterviewRequestChangeRepository;
import org.capstone.job_fair.repositories.job_fair.job_fair_booth.AssignmentRepository;
import org.capstone.job_fair.services.interfaces.job_fair.InterviewService;
import org.capstone.job_fair.services.interfaces.notification.NotificationService;
import org.capstone.job_fair.services.mappers.attendant.application.ApplicationMapper;
import org.capstone.job_fair.services.mappers.job_fair.InterviewRequestChangeMapper;
import org.capstone.job_fair.services.mappers.job_fair.InterviewScheduleMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@Slf4j
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
    @Qualifier("LocalNotificationService")
    private NotificationService notificationService;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private DynamoDBMapperConfig dynamoDBMapperConfig;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Value("${interview.length.millis}")
    private long interviewLength;

    @Value("${interview.buffer.millis}")
    private long interviewBufferLength;

    @Autowired
    private Clock clock;


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
        long now = clock.millis();
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
    protected void sendWaitingCountToConnectedUser(String channelId, String userId, String interviewerId, boolean isLeaveRoom) {
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
        Optional<ApplicationEntity> applicationOpt;
        if (isAttendant) {
            applicationOpt = applicationRepository.findByWaitingRoomIdAndAttendantAccountId(channelId, userId);
        } else {
            applicationOpt = applicationRepository.findByWaitingRoomIdAndInterviewerAccountId(channelId, userId);
        }

        if (!applicationOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.APPLICATION_NOT_FOUND));
        }
        ApplicationEntity application = applicationOpt.get();

        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient, dynamoDBMapperConfig);
        WaitingRoomVisitEntity entity = new WaitingRoomVisitEntity();
        entity.setUserId(userId);
        entity.setAttendant(isAttendant);
        entity.setChannelId(channelId);
        dynamoDBMapper.save(entity);


        this.sendWaitingCountToConnectedUser(channelId, userId, application.getInterviewer().getAccountId(), false);
    }

    @Override
    public void leaveWaitingRoom(String channelId, String userId, boolean isAttendant) {
        Optional<ApplicationEntity> applicationOpt;
        if (isAttendant) {
            applicationOpt = applicationRepository.findByWaitingRoomIdAndAttendantAccountId(channelId, userId);
        } else {
            applicationOpt = applicationRepository.findByWaitingRoomIdAndInterviewerAccountId(channelId, userId);
        }
        if (!applicationOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.APPLICATION_NOT_FOUND));
        }
        ApplicationEntity application = applicationOpt.get();


        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient, dynamoDBMapperConfig);

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
        try {
            DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient, dynamoDBMapperConfig);
            Map<String, AttributeValue> eav = new HashMap<>();
            eav.put(":channelId", new AttributeValue().withS(channelId));

            DynamoDBQueryExpression<WaitingRoomVisitEntity> queryExpression = new DynamoDBQueryExpression<WaitingRoomVisitEntity>()
                    .withKeyConditionExpression("channelId = :channelId")
                    .withExpressionAttributeValues(eav);


            List<WaitingRoomVisitEntity> scanResult = dynamoDBMapper.query(WaitingRoomVisitEntity.class, queryExpression);
            List<String> userIds = scanResult.stream().map(WaitingRoomVisitEntity::getUserId).collect(Collectors.toList());
            return userIds;
        } catch (SdkClientException ex) {
            log.error(InterviewServiceImpl.class.getSimpleName() + ": " + ex.getMessage());
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    @SneakyThrows
    @Transactional
    public void askAttendantJoinInterviewRoom(String attendantId, String interviewRoomId) {
        List<ApplicationEntity> applicationList = applicationRepository.findByInterviewRoomId(interviewRoomId);
        if (applicationList.size() == 0) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Interview.INTERVIEW_ROOM_NOT_FOUND));
        }
        ApplicationEntity firstApplication = applicationList.get(0);
        String waitingRoomId = firstApplication.getWaitingRoomId();
        List<String> connectedUserIds = this.getConnectedUserIds(waitingRoomId);
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
        long now = clock.millis();
        LocalDate localDate = LocalDate.now();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);
        long endTime = endOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        List<ApplicationEntity> applicationList = applicationRepository.findWaitingAttendant(waitingRoomId, InterviewStatus.INTERVIEWING, now, endTime);
        return applicationList.size();
    }

    @Override
    public List<InterviewScheduleDTO> getInterviewScheduleInWaitingRoom(String waitingRoomId) {
        List<ApplicationEntity> applicationList = applicationRepository.findWaitingAttendantByWaitingRoomId(waitingRoomId);
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
        return application;
    }

    @Override
    @Transactional
    public void finishInterview(String attendantId, String interviewRoomId, String reviewerId) {
        ApplicationEntity application = getValidApplicationEntity(attendantId, interviewRoomId, reviewerId);
        if (application.getInterviewStatus() == null || application.getInterviewStatus() != InterviewStatus.SUBMITTED_REPORT) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Interview.INVALID_STATUS));
        }
        application.setInterviewStatus(InterviewStatus.DONE);
        applicationRepository.save(application);
    }

    @Override
    @Transactional
    public void startInterview(String attendantId, String interviewRoomId, String reviewerId) {
        ApplicationEntity application = getValidApplicationEntity(attendantId, interviewRoomId, reviewerId);
        if (application.getInterviewStatus() == null || application.getInterviewStatus() != InterviewStatus.NOT_YET) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Interview.INVALID_STATUS));
        }
        application.setInterviewStatus(InterviewStatus.INTERVIEWING);
        applicationRepository.save(application);
    }

    @Data
    @AllArgsConstructor
    public static class Schedule {
        private long beginTime;
        private long endTime;
    }


    /***
     *
     * since the schedule list is already sort by beginTime
     * iterate through the schedule list and get the interval between these schedule
     * also calculate the interval between the beginTime and the first schedule's begin time
     * also calculate the interval between the endTime and the last schedule's end time
     *
     */
    private List<Schedule> getFreeSchedule(long beginTime, long endTime, List<ApplicationEntity> scheduleList){
        List<Schedule> result = new ArrayList<>();
        if (scheduleList.isEmpty()){
            result.add(new Schedule(beginTime, endTime));
            return result;
        }
        long beginScheduleTime = beginTime;
        ApplicationEntity applicationSchedule = null;
        for (int i = 0; i < scheduleList.size(); i ++){
            applicationSchedule = scheduleList.get(i);
            result.add(new Schedule(beginScheduleTime, applicationSchedule.getBeginTime()));
            beginScheduleTime = applicationSchedule.getEndTime();
        }
        //check last schedule to end time
        if (applicationSchedule.getEndTime() < endTime){
            result.add(new Schedule(applicationSchedule.getEndTime(), endTime));
        }

        return result;
    }


    /**
     * Inspired by this algorithm: https://www.geeksforgeeks.org/merging-intervals/
     * Step 1: merge 2 schedule list
     * Step 2: sort the merged schedule list by begin time
     * Step 3: currentSchedule = the first schedule from the list
     * Step 4: loop from the 2nd schedule to the last. For each schedule do the following steps
     *    Step 4.1: if the current and the next schedule is non overlapped, current schedule = next schedule
     *              else 4.1.1
     *       Step 4.1.1: beginTime = nextSchedule.beginTime
     *                   endTime = min(nextSchedule.endTime, currentSchedule.endTime)
     *       Step 4.1.2: add beginTime and endTime schedule to the result list
     *       Step 4.1.3: newCurrentScheduleBeginTime = endTime;
     *                   newCurrentScheduleEndTime = max(nextSchedule.endTime, currentSchedule.endTime);
     *       Step 4.1.4: currentSchedule = schedule( newCurrentScheduleBeginTime, newCurrentScheduleEndTime)
     *
     *    Ex for 4.1.1
     *    Case 1: currentSchedule: |----------|
     *            nextSchedule:        |----------|
     *            overlapSchedule:     |------|
     *    Case 2: currentSchedule: |------------------|
     *            nextSchedule:        |----------|
     *            overlapSchedule:     |----------|
     *    Case 3: currentSchedule: |--------------|
     *            nextSchedule:        |----------|
     *            overlapSchedule:     |----------|
     */
    private List<Schedule> getAvailableSchedule(List<Schedule> interviewerSchedules, List<Schedule> attendantSchedules){
        if (interviewerSchedules.isEmpty()){
            return attendantSchedules;
        }
        if (attendantSchedules.isEmpty()){
            return interviewerSchedules;
        }
        //step 1
        List<Schedule> mergeSchedules = Stream.concat(interviewerSchedules.stream(), attendantSchedules.stream()).collect(Collectors.toList());
        //step 2
        mergeSchedules.sort((o1, o2) -> Math.toIntExact(o1.beginTime - o2.beginTime));

        List<Schedule> result = new ArrayList<>();
        int index = 0;
        //step 3
        Schedule currentSchedule = mergeSchedules.get(0);
        //step 4
        while (index < mergeSchedules.size() - 1){
            Schedule nextSchedule = mergeSchedules.get(index + 1);
            //step 4.1
            if (nextSchedule.beginTime <= currentSchedule.endTime && nextSchedule.beginTime >= currentSchedule.beginTime){
                //overlap schedule
                long beginTime = nextSchedule.beginTime;
                long endTime = Math.min(nextSchedule.endTime, currentSchedule.endTime);
                result.add(new Schedule(beginTime, endTime));
                long newCurrentScheduleBeginTime = endTime;
                long newCurrentScheduleEndTime = Math.max(nextSchedule.endTime, currentSchedule.endTime);
                currentSchedule = new Schedule(newCurrentScheduleBeginTime, newCurrentScheduleEndTime);
            } else {
                //non-overlap schedule
                currentSchedule = nextSchedule;
            }
            index ++;
        }
        return result;
    }

    /***
     *
     * process of scheduling an interview
     * Step 1: Get interview assignments that endTime > current + interviewLength + bufferTime
     *         If empty throw Exception
     * Step 2: Iterate through the assignments. For each assignment do the following steps
     *     Step 2.1: get interviewer interview schedule
     *               get attendant interview schedule
     *     Step 2.2: get interviewer free schedule
     *               get attendant free schedule
     *     Step 2.3: find common free schedule from interviewer and attendant
     *     Step 2.4: if the common free schedule is empty move to next assignment
     *               else iterate through the free schedule. For each schedule do the following steps
     *        Step 2.4.1: get the free schedule length
     *        Step 2.4.2: if the length < interviewLength + bufferTime, continue
     *                    else step 2.4.2.1
     *           Step 2.4.2.1: interviewBeginTime = schedule.getBeginTime() + interviewBufferLength;
     *                         interviewEndTime = interviewBeginTime + interviewLength;
     *           Step 2.4.2.2: if the interviewer interview schedule is empty, generate random interviewRoomId, waitingRoomId
     *                         else get interviewRoomId, waitingRoomId from last interview schedule
     *
     */
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
        JobFairBoothEntity jobFairBooth = application.getBoothJobPosition().getJobFairBooth();

        long now = clock.millis();
        //Step 1
        List<AssignmentEntity> assignments = assignmentRepository.findByCompanyEmployeeAccountIdAndJobFairBoothId(interviewerId, jobFairBooth.getId());

        assignments = assignments.stream().filter(assignment -> {
            if (assignment.getType() != AssignmentType.INTERVIEWER) return false;
            return assignment.getEndTime() > now + interviewLength + interviewBufferLength;
        }).collect(Collectors.toList());
        assignments.sort((o1, o2) -> {
            return Math.toIntExact(o1.getBeginTime() - o2.getBeginTime());
        });
        if (assignments.isEmpty()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.InterviewSchedule.MAXIMUM_SCHEDULE_ALLOW));
        }

        //step 2
        int currentAssignmentSlot = 0;
        String interviewRoomId = "";
        String waitingRoomId = "";
        long interviewBeginTime = 0;
        long interviewEndTime = 0;
        assignmentLoop:
        while (currentAssignmentSlot < assignments.size()){
            AssignmentEntity currentAssignment = assignments.get(currentAssignmentSlot);
            //step 2.1
            List<ApplicationEntity> interviewerScheduleList = applicationRepository.findWholeByInterviewerAndInTimeRange(interviewerId, currentAssignment.getBeginTime(), currentAssignment.getEndTime());
            List<ApplicationEntity> attendantScheduleList = applicationRepository.findWholeByAttendantAndInTimeRange(application.getAttendant().getAccountId(), currentAssignment.getBeginTime(), currentAssignment.getEndTime());
            interviewerScheduleList.sort((o1, o2) -> Math.toIntExact(o1.getBeginTime() - o2.getBeginTime()));
            attendantScheduleList.sort((o1, o2) -> Math.toIntExact(o1.getBeginTime() - o2.getBeginTime()));
            //step 2.2
            List<Schedule> interviewerFreeSchedule = getFreeSchedule(currentAssignment.getBeginTime(), currentAssignment.getEndTime(), interviewerScheduleList);
            List<Schedule> attendantFreeSchedule = getFreeSchedule(currentAssignment.getBeginTime(), currentAssignment.getEndTime(), attendantScheduleList);
            //step 2.3
            List<Schedule> availableFreeSchedule = getAvailableSchedule(interviewerFreeSchedule, attendantFreeSchedule);
            //step 2.4
            if (availableFreeSchedule.isEmpty()){
                currentAssignmentSlot++;
                continue;
            }
            for (Schedule schedule : availableFreeSchedule){
                //step 2.4.1
                long freeTimeLength = schedule.endTime - schedule.beginTime;
                //step 2.4.2
                if (freeTimeLength < interviewLength + interviewBufferLength) continue;
                //has time slot for interview
                interviewBeginTime = schedule.getBeginTime() + interviewBufferLength;
                interviewEndTime = interviewBeginTime + interviewLength;
                if (interviewerScheduleList.isEmpty()){
                    interviewRoomId =  ScheduleConstant.INTERVIEW_ROOM_PREFIX + UUID.randomUUID().toString();
                    waitingRoomId = ScheduleConstant.WAITING_ROOM_PREFIX + UUID.randomUUID().toString();
                } else {
                    ApplicationEntity lastInterview = interviewerScheduleList.get(interviewerScheduleList.size() -1);
                    interviewRoomId =  lastInterview.getInterviewRoomId();
                    waitingRoomId = lastInterview.getWaitingRoomId();
                }
                break assignmentLoop;

            }
            currentAssignmentSlot++;
        }

        if (interviewRoomId.isEmpty()){
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.InterviewSchedule.MAXIMUM_SCHEDULE_ALLOW));
        }

        application.setInterviewName("Interview with " + application.getAttendant().getAccount().getFullname());
        application.setInterviewDescription("Interview with " + application.getAttendant().getAccount().getFullname());
        application.setBeginTime(interviewBeginTime);
        application.setEndTime(interviewEndTime);
        application.setInterviewStatus(InterviewStatus.NOT_YET);
        CompanyEmployeeEntity companyEmployee = new CompanyEmployeeEntity();
        companyEmployee.setAccountId(interviewerId);
        application.setInterviewer(companyEmployee);
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
    public Optional<InterviewScheduleDTO> getCurrentScheduleByEmployeeIdAndInterviewRoomId(String employeeId, String interviewRoomId) {
        List<ApplicationEntity> applicationList = applicationRepository.findByInterviewRoomId(interviewRoomId);
        long now = clock.millis();
        for (ApplicationEntity application : applicationList) {
            if (now < application.getEndTime() && now > application.getBeginTime()) {
                return Optional.of(interviewScheduleMapper.toDTO(application));
            }
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void swapSchedule(String fromApplicationId, String toApplicationId) {
        Optional<ApplicationEntity> fromApplicationOpt = applicationRepository.findById(fromApplicationId);
        if (!fromApplicationOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.APPLICATION_NOT_FOUND));
        }
        Optional<ApplicationEntity> toApplicationOpt = applicationRepository.findById(toApplicationId);
        if (!toApplicationOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.APPLICATION_NOT_FOUND));
        }
        ApplicationEntity fromApplication = fromApplicationOpt.get();
        ApplicationEntity toApplication = toApplicationOpt.get();

        if (fromApplication.getInterviewStatus() != InterviewStatus.NOT_YET || toApplication.getInterviewStatus() != InterviewStatus.NOT_YET) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Interview.INVALID_STATUS));
        }

        long fromBeginTime = fromApplication.getBeginTime();
        long fromEndTime = fromApplication.getEndTime();

        fromApplication.setBeginTime(toApplication.getBeginTime());
        fromApplication.setEndTime(toApplication.getEndTime());

        toApplication.setBeginTime(fromBeginTime);
        toApplication.setEndTime(fromEndTime);

        applicationRepository.save(fromApplication);
        applicationRepository.save(toApplication);
    }

    @Override
    @Transactional
    public ApplicationDTO createInterviewReport(String applicationId, String advantage, String disadvantage, String note) {
        Optional<ApplicationEntity> applicationOpt = applicationRepository.findById(applicationId);
        if (!applicationOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.APPLICATION_NOT_FOUND));
        }
        ApplicationEntity application = applicationOpt.get();
        InterviewStatus interviewStatus = application.getInterviewStatus();
        if (interviewStatus == null || interviewStatus == InterviewStatus.NOT_YET) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Interview.INVALID_STATUS));
        }
        application.setInterviewStatus(InterviewStatus.SUBMITTED_REPORT);
        application.setAttendantAdvantage(advantage);
        application.setAttendantDisadvantage(disadvantage);
        application.setInterviewNote(note);
        applicationRepository.save(application);
        return applicationMapper.toDTO(application);

    }


    @Override
    public List<InterviewScheduleDTO> getScheduleByInterviewRoomId(String interviewRoomId) {
        return applicationRepository.findByInterviewRoomId(interviewRoomId).stream().map(interviewScheduleMapper::toDTO).collect(Collectors.toList());
    }

}
