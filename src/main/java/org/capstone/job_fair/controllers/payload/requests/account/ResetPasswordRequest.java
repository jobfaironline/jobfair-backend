package org.capstone.job_fair.controllers.payload.requests.account;

import lombok.*;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PasswordConstraint;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResetPasswordRequest {
    @NonNull
    private String otp;
    @EmailConstraint
    @NotNull
    private String email;
    @PasswordConstraint
    @NotNull
    private String newPassword;
    @NotNull
    @PasswordConstraint
    private String confirmPassword;
}
