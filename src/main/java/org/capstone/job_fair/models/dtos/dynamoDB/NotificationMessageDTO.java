package org.capstone.job_fair.models.dtos.dynamoDB;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.enums.NotificationType;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NotificationMessageDTO implements Serializable {
    private String id;
    private String notificationId;
    private NotificationType notificationType;
    private String title;
    private String message;
    private boolean isRead;
    private Long createDate;
    private String userId;
}
