package org.capstone.job_fair.controllers.payload.requests;


import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CancelCompanyJobFairRegistrationRequest {
    @NotNull
    private String companyRegistrationId;

    @NotNull
    private String cancelReason;

}
