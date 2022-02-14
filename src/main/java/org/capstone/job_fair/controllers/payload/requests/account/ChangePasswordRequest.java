package org.capstone.job_fair.controllers.payload.requests.account;

import lombok.Data;
import org.capstone.job_fair.validators.PasswordConstraint;

import javax.validation.constraints.NotNull;

@Data
public class ChangePasswordRequest {
    @NotNull
    private String oldPassword;
    @NotNull
    @PasswordConstraint
    private String newPassword;
}
