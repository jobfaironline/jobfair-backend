package org.capstone.job_fair.controllers.payload.responses;

import lombok.*;
import org.capstone.job_fair.models.statuses.AccountStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private String email;
    private String password;
    private AccountStatus status;
    private String roles;
    private String token;
    private String refreshToken;
    private boolean isEmployeeFirstTime;
    private String userId;
    private String companyId;
}
