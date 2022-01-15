package org.capstone.job_fair.controllers.payload.responses;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RefreshTokenResponse {
    private String refreshToken;
    private String token;
}
