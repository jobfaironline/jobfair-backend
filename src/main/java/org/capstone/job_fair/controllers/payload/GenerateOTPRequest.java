package org.capstone.job_fair.controllers.payload;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GenerateOTPRequest {
    private String email;
}
