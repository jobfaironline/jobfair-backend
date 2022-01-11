package org.capstone.job_fair.payload;

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
        Long timestamp = Instant.now().getEpochSecond();
        Map<String, String> body = new HashMap<>();
        body.put("message", message);
        body.put("timestamp", timestamp.toString());
        return new ResponseEntity<>(body, status);

    }
}