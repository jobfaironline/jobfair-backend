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
public class NotificationMessageDTO implements Serializable {
    private String notificationId;
    private String notificationType;
    private String title;
    private String message;
    private boolean isRead;
    private long createDate;
}
