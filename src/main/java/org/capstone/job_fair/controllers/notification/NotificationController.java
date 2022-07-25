package org.capstone.job_fair.controllers.notification;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.dtos.dynamoDB.NotificationMessageDTO;
import org.capstone.job_fair.models.enums.NotificationType;
import org.capstone.job_fair.services.interfaces.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class NotificationController {

    @Autowired
    @Qualifier("LocalNotificationService")
    private NotificationService notificationService;

    @GetMapping(ApiEndPoint.Notification.NOTIFICATION)
    public ResponseEntity<?> getNotification() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(notificationService.getNotificationByAccountId(userDetails.getId()));
    }

    @PostMapping(ApiEndPoint.Notification.READ + "/{id}")
    public ResponseEntity<?> readNotification(@PathVariable("id") String id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        notificationService.readNotification(id, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping(ApiEndPoint.Notification.READ_ALL)
    public ResponseEntity<?> readNotification() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        notificationService.readAll(userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/notification")
    public ResponseEntity<?> makeNoti(@RequestParam String message, @RequestParam String title, @RequestParam String userId, @RequestParam NotificationType type) {
        NotificationMessageDTO dto = NotificationMessageDTO.builder().message(message).title(title).userId(userId).notificationType(type).build();
        notificationService.createNotification(dto, userId);
        return ResponseEntity.ok().build();
    }
}
