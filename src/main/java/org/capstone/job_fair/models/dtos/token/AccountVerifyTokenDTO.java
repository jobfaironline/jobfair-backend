package org.capstone.job_fair.models.dtos.token;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AccountVerifyTokenDTO implements Serializable {
    @EqualsAndHashCode.Include
    private String id;
    private String accountId;
    private Boolean isInvalidated;
    private Long createdTime;
    private Long expiredTime;
}
