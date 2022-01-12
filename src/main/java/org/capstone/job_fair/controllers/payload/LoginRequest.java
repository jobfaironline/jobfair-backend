package org.capstone.job_fair.controllers.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PasswordConstraint;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @EmailConstraint
    private String email;
    @PasswordConstraint
    private String password;
}
