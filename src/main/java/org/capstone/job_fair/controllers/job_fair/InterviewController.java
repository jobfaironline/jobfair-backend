package org.capstone.job_fair.controllers.job_fair;

import com.amazonaws.util.json.Jackson;
import lombok.SneakyThrows;
import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.constants.ScheduleConstant;
import org.capstone.job_fair.controllers.payload.requests.job_fair.ChangeInterviewScheduleRequest;
import org.capstone.job_fair.controllers.payload.requests.job_fair.CreateInterviewReportRequest;
import org.capstone.job_fair.controllers.payload.responses.EmployeeWaitingRoomScheduleResponse;
import org.capstone.job_fair.models.dtos.attendant.application.ApplicationDTO;
import org.capstone.job_fair.models.dtos.dynamoDB.NotificationMessageDTO;
import org.capstone.job_fair.models.dtos.job_fair.InterviewRequestChangeDTO;
import org.capstone.job_fair.models.dtos.job_fair.InterviewScheduleDTO;
import org.capstone.job_fair.models.enums.InterviewRequestChangeStatus;
import org.capstone.job_fair.models.enums.NotificationType;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.services.interfaces.job_fair.InterviewService;
import org.capstone.job_fair.services.interfaces.notification.NotificationService;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class InterviewController {

    @Autowired
    @Qualifier("LocalInterviewService")
    private InterviewService interviewService;

    @Autowired
    @Qualifier("LocalNotificationService")
    private NotificationService notificationService;

    @Value("${clock.delay.days}")
    private int delay;

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @GetMapping(ApiEndPoint.Interview.SCHEDULE)
    public ResponseEntity<?> getInterviewSchedule(@RequestParam(defaultValue = ScheduleConstant.BEGIN_TIME) Long beginTime,
                                                  @RequestParam(defaultValue = ScheduleConstant.END_TIME) Long endTime) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<InterviewScheduleDTO> result = Collections.emptyList();
        if (userDetails.hasRole(Role.ATTENDANT)) {
            result = interviewService.getInterviewScheduleForAttendant(userDetails.getId(), beginTime, endTime);
        }
        if (userDetails.hasRole(Role.COMPANY_EMPLOYEE)) {
            result = interviewService.getInterviewScheduleForCompanyEmployee(userDetails.getId(), beginTime, endTime);
        }
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }


    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @PostMapping(ApiEndPoint.Interview.REQUEST_CHANGE)
    public ResponseEntity<?> requestChangeInterviewSchedule(@Valid @RequestBody ChangeInterviewScheduleRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAttendant = userDetails.hasRole(Role.ATTENDANT);
        InterviewRequestChangeDTO requestChange = interviewService.requestChangeSchedule(request.getApplicationId(), userDetails.getId(), isAttendant, request.getRequestMessage(), request.getBeginTime(), request.getEndTime());

        NotificationMessageDTO message = NotificationMessageDTO.builder()
                .message(MessageUtil.getMessage(MessageConstant.NotificationMessage.REQUEST_CHANGE_INTERVIEW.MESSAGE))
                .title(MessageUtil.getMessage(MessageConstant.NotificationMessage.REQUEST_CHANGE_INTERVIEW.TITLE))
                .notificationType(NotificationType.NOTI).build();
        notificationService.createNotification(message, requestChange.getApplication().getInterviewer().getAccountId());
        return ResponseEntity.ok(requestChange);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    @PostMapping(ApiEndPoint.Interview.EVALUATE_REQUEST_CHANGE + "/{id}")
    public ResponseEntity<?> evaluateRequestChange(@PathVariable("id") String id, @RequestParam InterviewRequestChangeStatus status) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InterviewRequestChangeDTO requestChange = interviewService.evaluateRequestChange(id, status, userDetails.getId());
        return ResponseEntity.ok(requestChange);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @GetMapping(ApiEndPoint.Interview.LATEST_REQUEST_CHANGE)
    public ResponseEntity<?> getLatestRequestChangeByApplication(@RequestParam("applicationId") String applicationId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAttendant = userDetails.hasRole(Role.ATTENDANT);
        return interviewService.getLatestRequestChangeByApplication(applicationId, userDetails.getId(), isAttendant)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @GetMapping(ApiEndPoint.Interview.REQUEST_CHANGE)
    public ResponseEntity<?> getRequestChangeList(@RequestParam("applicationId") String applicationId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAttendant = userDetails.hasRole(Role.ATTENDANT);
        List<InterviewRequestChangeDTO> result = interviewService.getRequestChangesByApplication(applicationId, userDetails.getId(), isAttendant);
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }


    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @PostMapping(ApiEndPoint.Interview.VISIT_WAITING_ROOM)
    public ResponseEntity<?> visitWaitingRoom(@RequestParam("channelId") String channelId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAttendant = userDetails.hasRole(Role.ATTENDANT);
        interviewService.visitWaitingRoom(channelId, userDetails.getId(), isAttendant);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @PostMapping(ApiEndPoint.Interview.LEAVE_WAITING_ROOM)
    public ResponseEntity<?> leaveWaitingRoom(@RequestParam("channelId") String channelId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAttendant = userDetails.hasRole(Role.ATTENDANT);
        interviewService.leaveWaitingRoom(channelId, userDetails.getId(), isAttendant);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    @PostMapping(ApiEndPoint.Interview.ASK_TO_JOIN_INTERVIEW_ROOM)
    public ResponseEntity<?> askToJoinInterviewRoom(
            @RequestParam("attendantId") String attendantId,
            @RequestParam("interviewRoomId") String interviewRoomId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<InterviewScheduleDTO> currentSchedule = interviewService.getCurrentScheduleByEmployeeIdAndInterviewRoomId(
                userDetails.getId(), interviewRoomId
        );
        if (delay > 0){
            interviewService.askAttendantJoinInterviewRoom(attendantId, interviewRoomId);
            return ResponseEntity.ok().build();
        }
        if (currentSchedule.isPresent()) {
            if (!currentSchedule.get().getAttendantId().equals(attendantId)) {
                return ResponseEntity.accepted().build();
            }
        }
        interviewService.askAttendantJoinInterviewRoom(attendantId, interviewRoomId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @GetMapping(ApiEndPoint.Interview.GET_WAITING_ROOM_INFO)
    public ResponseEntity<?> getWaitingRoomInformation(@RequestParam("waitingRoomId") String waitingRoomId) {
        List<InterviewScheduleDTO> scheduleDTOS = interviewService.getInterviewScheduleInWaitingRoom(waitingRoomId);
        List<String> connectedUserId = interviewService.getConnectedUserIds(waitingRoomId);
        List<EmployeeWaitingRoomScheduleResponse> result = scheduleDTOS.stream().map(dto -> {
            EmployeeWaitingRoomScheduleResponse response = new EmployeeWaitingRoomScheduleResponse(dto);
            response.setInWaitingRoom(connectedUserId.contains(dto.getAttendantId()));
            return response;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping(ApiEndPoint.Interview.FINISH_INTERVIEW)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> finishInterview(@RequestParam("attendantId") String attendantId,
                                             @RequestParam("interviewRoomId") String interviewRoomId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        interviewService.finishInterview(attendantId, interviewRoomId, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping(ApiEndPoint.Interview.START_INTERVIEW)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> startInterview(@RequestParam("attendantId") String attendantId,
                                            @RequestParam("interviewRoomId") String interviewRoomId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        interviewService.startInterview(attendantId, interviewRoomId, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    @GetMapping(ApiEndPoint.Interview.INTERVIEW_ROOM + "/{id}")
    public ResponseEntity<?> getScheduleByInterviewRoomId(@PathVariable("id") String id) {
        List<InterviewScheduleDTO> result = interviewService.getScheduleByInterviewRoomId(id);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @GetMapping(ApiEndPoint.Interview.SCHEDULE + "/{id}")
    public ResponseEntity<?> getScheduleById(@PathVariable("id") String id) {
        Optional<InterviewScheduleDTO> scheduleOpt = interviewService.getScheduleById(id);
        if (!scheduleOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(scheduleOpt.get());
    }

    @PostMapping(ApiEndPoint.Interview.SWAP_INTERVIEW)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> swapInterviewSlot(@RequestParam("fromApplicationId") String fromApplicationId,
                                               @RequestParam("toApplicationId") String toApplicationId) {
        interviewService.swapSchedule(fromApplicationId, toApplicationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(ApiEndPoint.Interview.KICK_USER)
    @SneakyThrows
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> kickUser(@RequestParam("userId") String userId, @RequestParam("channelId") String channelId) {

        Map<String, String> payload = new HashMap<>();
        payload.put("userId", userId);
        payload.put("channelId", channelId);


        NotificationMessageDTO message = NotificationMessageDTO.builder()
                .message(Jackson.getObjectMapper().writeValueAsString(payload))
                .title("Kick user")
                .notificationType(NotificationType.KICK_USER)
                .build();

        notificationService.createNotification(message, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(ApiEndPoint.Interview.REPORT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> createReport(@RequestBody CreateInterviewReportRequest request) {
        ApplicationDTO application = interviewService.createInterviewReport(request.getApplicationId(), request.getAdvantage(), request.getDisadvantage(), request.getNote(), request.isQualified());
        return ResponseEntity.ok(application);
    }


}
