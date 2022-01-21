package org.capstone.job_fair.models.dtos.company;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaDTO {
    private String id;
    private String url;
    public MediaDTO(String url) {
        this.url = url;
    }
}
