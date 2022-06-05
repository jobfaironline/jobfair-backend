package org.capstone.job_fair.models.dtos.job_fair;

import lombok.*;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.statuses.JobFairPlanStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class JobFairDTO implements Serializable {
    @EqualsAndHashCode.Include
    private String id;
    @NotNull
    private Long createTime;
    @NotNull
    private Long decorateStartTime;
    @NotNull
    private Long decorateEndTime;
    @NotNull
    private Long publicStartTime;
    @NotNull
    private Long publicEndTime;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private String targetAttendant;
    private String thumbnailUrl;
    @NotNull
    private JobFairPlanStatus status;
    private String cancelReason;
    @NotNull
    private String hostName;
    private CompanyDTO company;
    private List<ShiftDTO> shifts;
}
