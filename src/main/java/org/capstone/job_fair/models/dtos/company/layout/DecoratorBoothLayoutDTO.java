package org.capstone.job_fair.models.dtos.company.layout;

import lombok.*;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DecoratorBoothLayoutDTO implements Serializable {
    @EqualsAndHashCode.Include
    private String id;
    private String url;
    private String name;
    private Long createTime;
    private CompanyEmployeeDTO companyEmployee;
    private List<DecoratorBoothLayoutVideoDTO> decoratorBoothLayoutVideos;
}
