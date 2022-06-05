package org.capstone.job_fair.models.dtos.company.layout;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DecoratorBoothLayoutVideoDTO {
    @EqualsAndHashCode.Include
    private String id;
    private String url;
    private String itemName;
    private String decoratorBoothLayoutId;
}
