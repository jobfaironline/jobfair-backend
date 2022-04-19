package org.capstone.job_fair.controllers.notification;


import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.services.impl.notification.NotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @Autowired
    private NotificationServiceImpl notificationServiceImpl;

    @GetMapping(ApiEndPoint.Notification.NOTIFICATION_ENDPOINT)
    public ResponseEntity<?> create() {
//        notificationServiceImpl.createNoti();
        return ResponseEntity.ok().build();
    }
}
