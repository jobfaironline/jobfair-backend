package org.capstone.job_fair.controllers.payload.requests.attendant;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCountryRequest {
    private String name;
}
