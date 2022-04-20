package org.capstone.job_fair.controllers.notification;


import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.dtos.notification.NotificationDTO;
import org.capstone.job_fair.models.dtos.notification.NotificationMessageDTO;
import org.capstone.job_fair.services.impl.notification.NotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
public class NotificationController {

    @Autowired
    private NotificationServiceImpl notificationServiceImpl;

    @GetMapping(ApiEndPoint.Notification.NOTIFICATION_ENDPOINT)
    public ResponseEntity<?> create() {
        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        NotificationDTO dto = null;
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        NotificationMessageDTO messageDTO = new NotificationMessageDTO(null,"noti","Message title", "Test message",false,0);
        notificationServiceImpl.createNotification(messageDTO, Arrays.asList(userDetails.getId()));
        return ResponseEntity.ok().build();
    }
}
