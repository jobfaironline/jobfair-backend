package org.capstone.job_fair.controllers.payload.requests.attendant;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateResidenceRequest {
    @NotBlank
    private String id;
    @NotBlank
    private String name;
}
