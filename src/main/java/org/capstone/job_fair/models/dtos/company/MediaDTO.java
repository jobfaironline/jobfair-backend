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
    private Integer id;
    private String url;
    private String description;
    private String companyId;
    public MediaDTO(String url) {
        this.url = url;
    }
}
