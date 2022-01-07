package org.capstone.job_fair.models.dtos.company;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaDTO {
    private String id;
    private String url;
    private String description;
}
