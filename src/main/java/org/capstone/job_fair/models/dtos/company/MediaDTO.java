package org.capstone.job_fair.models.dtos.company;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MediaDTO {
    @EqualsAndHashCode.Include
    private String id;
    private String url;
    private CompanyDTO companyDTO;

    public MediaDTO(String url) {
        this.url = url;
    }
}
