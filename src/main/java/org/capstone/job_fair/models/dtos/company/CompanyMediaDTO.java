package org.capstone.job_fair.models.dtos.company;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CompanyMediaDTO {
    @EqualsAndHashCode.Include
    private String id;
    private String url;
    private CompanyDTO company;

    public CompanyMediaDTO(String url) {
        this.url = url;
    }
}
