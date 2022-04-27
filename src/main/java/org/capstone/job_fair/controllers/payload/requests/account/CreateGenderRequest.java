package org.capstone.job_fair.controllers.payload.requests.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateGenderRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
}
