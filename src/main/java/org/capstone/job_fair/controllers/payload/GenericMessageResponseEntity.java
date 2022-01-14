package org.capstone.job_fair.controllers.payload;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


@Getter
@Setter
public class GenericMessageResponseEntity{
    public static ResponseEntity<?> build(String message, HttpStatus status) {
        long timestamp = Instant.now().getEpochSecond();
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("message", message);
        body.put("timestamp", timestamp);
        return new ResponseEntity<>(body, status);

    }
}