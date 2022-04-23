package org.capstone.job_fair.controllers;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.services.interfaces.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @Autowired
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

    @PostMapping(ApiEndPoint.Notification.READ_ALL )
    public ResponseEntity<?> readNotification() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        notificationService.readAll(userDetails.getId());
        return ResponseEntity.ok().build();
    }
}
