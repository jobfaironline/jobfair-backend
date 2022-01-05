package org.capstone.job_fair.payload;

import lombok.*;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private String email;
    private String password;
    private int status;
    private Collection<String> roles;
    private String token;
}
