package org.capstone.job_fair.payload;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResetPasswordResponse {
    private String email;
    private String newPassword;
    private String message;
}
