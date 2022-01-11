package org.capstone.job_fair.payload;

import lombok.*;
import org.capstone.job_fair.models.statuses.AccountStatus;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private String email;
    private String password;
    private AccountStatus status;
    private Collection<String> roles;
    private String token;
    private String refreshToken;


}
