package org.capstone.job_fair.payload;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResetPasswordTokenRequest {
    private String email;
}
