package org.capstone.job_fair.models.dtos.token;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountVerifyTokenDTO implements Serializable {
    private String id;
    private String accountId;
    private Boolean isInvalidated;
    private Long createdTime;
    private Long expiredTime;
}
