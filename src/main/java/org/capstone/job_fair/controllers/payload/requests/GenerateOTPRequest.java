package org.capstone.job_fair.controllers.payload.requests;

import lombok.*;
import org.capstone.job_fair.validators.EmailConstraint;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GenerateOTPRequest {
    @NotNull
    @EmailConstraint
    private String email;
}
