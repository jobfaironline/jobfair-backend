package org.capstone.job_fair.services.interfaces.job_fair;

import org.capstone.job_fair.models.dtos.job_fair.InterviewRequestChangeDTO;
import org.capstone.job_fair.models.dtos.job_fair.InterviewScheduleDTO;
import org.capstone.job_fair.models.entities.attendant.application.ApplicationEntity;
import org.capstone.job_fair.models.enums.InterviewRequestChangeStatus;

import java.util.List;
import java.util.Optional;

public interface InterviewService {
    List<InterviewScheduleDTO> getInterviewScheduleForCompanyEmployee(String employeeId, Long beginTime, Long endTime);

    List<InterviewScheduleDTO> getInterviewScheduleForAttendant(String attendantId, Long beginTime, Long endTime);

    InterviewRequestChangeDTO requestChangeSchedule(String applicationId, String userId, boolean isAttendant, String requestMessage, Long beginTime, Long endTime);

    InterviewRequestChangeDTO evaluateRequestChange(String requestChangeId, InterviewRequestChangeStatus status, String userId);

    Optional<InterviewRequestChangeDTO> getLatestRequestChangeByApplication(String applicationId, String userId, boolean isAttendant);

    List<InterviewRequestChangeDTO> getRequestChangesByApplication(String applicationId, String userId, boolean isAttendant);

    void visitWaitingRoom(String channelId, String userId, boolean isAttendant);

    void leaveWaitingRoom(String channelId, String userId, boolean isAttendant);

    List<String> getConnectedUserIds(String channelId);

    void askAttendantJoinInterviewRoom(String attendantId, String interviewRoomId);

    int getAttendantTurnInWaitingRoom(String attendantId, String waitingRoomId);

    List<InterviewScheduleDTO> getInterviewScheduleInWaitingRoom(String employeeId, String waitingId);

    void finishInterview(String attendantId, String interviewRoomId, String reviewerId);

    void startInterview(String attendantId, String interviewRoomid, String reviewerId);

    Optional<InterviewScheduleDTO> getScheduleByInterviewRoomId(String interviewRoomId);

    InterviewScheduleDTO scheduleInterview(String applicationId, String interviewerId);

}
