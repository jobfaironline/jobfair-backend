package org.capstone.job_fair.models.dtos.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SendToUserDTO implements Serializable {
    private String id;
    private String userId;
    private String notificationId;
}
