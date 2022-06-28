package org.capstone.job_fair.controllers.payload.requests.job_fair;

import lombok.*;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.validators.XSSConstraint;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UpdateJobFairRequest {
    @Data
    public static class ShiftRequest {
        @NotNull
        private Long beginTime;
        @NotNull
        private Long endTime;
    }

    @NotBlank
    private String id;

    @NumberFormat
    @NotNull
    @Min(DataConstraint.JobFair.MIN_TIME)
    private Long decorateStartTime;

    @NumberFormat
    @NotNull
    @Min(DataConstraint.JobFair.MIN_TIME)
    private Long decorateEndTime;

    @NumberFormat
    @NotNull
    @Min(DataConstraint.JobFair.MIN_TIME)
    private Long publicStartTime;

    @NumberFormat
    @NotNull
    @Min(DataConstraint.JobFair.MIN_TIME)
    private Long publicEndTime;

    @XSSConstraint
    @NotNull
    @Size(max = DataConstraint.JobFair.NAME_MAX_LENGTH)
    private String name;

    @XSSConstraint
    @Size(max = DataConstraint.JobFair.MAX_DESCRIPTION_LENGTH)
    private String description;


    @XSSConstraint
    @Size(max = DataConstraint.JobFair.TARGET_ATTENDANT_MAX_LENGTH)
    private String targetAttendant;

    @XSSConstraint
    @Size(max = DataConstraint.JobFair.HOST_NAME_MAX_LENGTH)
    private String hostName;

    private List<ShiftRequest> shifts;
}
