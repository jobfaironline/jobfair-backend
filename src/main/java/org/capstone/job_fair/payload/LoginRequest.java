package org.capstone.job_fair.payload;

import com.sun.istack.NotNull;
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
