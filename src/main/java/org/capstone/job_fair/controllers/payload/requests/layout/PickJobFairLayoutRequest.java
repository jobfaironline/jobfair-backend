package org.capstone.job_fair.controllers.payload.requests.layout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PickJobFairLayoutRequest {
    @NotBlank
    private String jobFairId;
    @NotBlank
    private String layoutId;
}
