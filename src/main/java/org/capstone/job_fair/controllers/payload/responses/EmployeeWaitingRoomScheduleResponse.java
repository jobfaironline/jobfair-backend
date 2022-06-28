package org.capstone.job_fair.controllers.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.dtos.job_fair.InterviewScheduleDTO;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeWaitingRoomScheduleResponse extends InterviewScheduleDTO {

    private boolean isInWaitingRoom;

    public EmployeeWaitingRoomScheduleResponse(InterviewScheduleDTO parent) {
        this.setId(parent.getId());
        this.setEndTime(parent.getEndTime());
        this.setBeginTime(parent.getBeginTime());
        this.setName(parent.getName());
        this.setDescription(parent.getDescription());
        this.setStatus(parent.getStatus());
        this.setInterviewerId(parent.getInterviewerId());
        this.setAttendantId(parent.getAttendantId());
        this.setJobFairPublicEndTime(parent.getJobFairPublicEndTime());
        this.setAttendantName(parent.getAttendantName());
        this.setInterviewRoomId(parent.getInterviewRoomId());
        this.setWaitingRoomId(parent.getWaitingRoomId());
    }
}
