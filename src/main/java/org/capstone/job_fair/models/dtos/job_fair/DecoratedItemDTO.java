package org.capstone.job_fair.models.dtos.job_fair;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DecoratedItemDTO {
    @EqualsAndHashCode.Include
    private String id;
    private Integer size;
    private String url;
    private String name;
    private String description;
}
