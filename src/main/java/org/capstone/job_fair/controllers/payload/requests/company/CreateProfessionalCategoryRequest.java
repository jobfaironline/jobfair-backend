package org.capstone.job_fair.controllers.payload.requests.company;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProfessionalCategoryRequest {
    private String name;
}
