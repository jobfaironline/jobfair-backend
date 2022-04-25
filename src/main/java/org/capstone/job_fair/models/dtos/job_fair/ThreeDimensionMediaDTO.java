package org.capstone.job_fair.models.dtos.job_fair;

import lombok.*;
import org.capstone.job_fair.models.statuses.ThreeDimensionMediaType;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ThreeDimensionMediaDTO {
    @EqualsAndHashCode.Include
    private String id;
    private String url;
    private String name;
    private String description;
    private ThreeDimensionMediaType type;
    private String thumbnailUrl;

}
