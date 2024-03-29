package org.capstone.job_fair.controllers.payload.requests.accesss_control;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {
    @NotNull
    private String token;
    @NotNull
    private String refreshToken;
}
