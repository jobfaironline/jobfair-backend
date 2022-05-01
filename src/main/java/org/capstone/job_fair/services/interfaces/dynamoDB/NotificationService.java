package org.capstone.job_fair.services.interfaces.dynamoDB;

import org.capstone.job_fair.models.dtos.dynamoDB.NotificationMessageDTO;
import org.capstone.job_fair.models.enums.Role;

import java.util.List;

public interface NotificationService {
    void createNotification(NotificationMessageDTO message, Role role);

    void createNotification(NotificationMessageDTO message, String receiverId);

    void createNotification(NotificationMessageDTO message, List<String> receiverIdList);

    List<NotificationMessageDTO> getNotificationByAccountId(String id);

    void readNotification(String id, String userId);

    void readAll(String userId);
}

