package org.capstone.job_fair.models.dtos.company;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CompanyBoothLayoutDTO implements Serializable {
    @EqualsAndHashCode.Include
    private String id;
    private Integer version;
    private Long createDate;
    @JsonBackReference
    private CompanyBoothDTO companyBooth;
    private String url;
    List<CompanyBoothLayoutVideoDTO> companyBoothLayoutVideos;
}
