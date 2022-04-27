package org.capstone.job_fair.controllers.payload.requests.attendant;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateJobLevelRequest {
    private String name;
}
