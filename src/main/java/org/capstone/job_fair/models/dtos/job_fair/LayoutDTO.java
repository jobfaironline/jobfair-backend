package org.capstone.job_fair.models.dtos.job_fair;

import lombok.*;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LayoutDTO implements Serializable {
    @EqualsAndHashCode.Include
    private String id;
    private String name;
    private String url;
    private String description;
    private Set<LayoutBoothDTO> booths;
    private CompanyDTO company;
    private String thumbnailUrl;
}
