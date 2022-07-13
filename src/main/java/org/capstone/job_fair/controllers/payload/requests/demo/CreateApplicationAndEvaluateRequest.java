package org.capstone.job_fair.controllers.payload.requests.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateApplicationAndEvaluateRequest {
    private List<String> cvId;
    private String employeeId;
}
