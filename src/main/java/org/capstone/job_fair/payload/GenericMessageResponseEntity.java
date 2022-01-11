package org.capstone.job_fair.payload;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;


@Getter
@Setter
public class GenericMessageResponseEntity extends ResponseEntity<String> {
    private String message;
    private Long timestamp;


    public GenericMessageResponseEntity(String body, HttpStatus status) {
        super(body, status);
        this.message = body;
        this.timestamp = Instant.now().getEpochSecond();
    }
}