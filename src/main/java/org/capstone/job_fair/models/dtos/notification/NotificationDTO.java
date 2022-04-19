package org.capstone.job_fair.models.dtos.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO implements Serializable {
    private String id;
    private String userId;
    private String title;
    private String message;
    private boolean isRead;
    private long createDate;

}
