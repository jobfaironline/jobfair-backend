package org.capstone.job_fair.models.dtos.attendant;

import lombok.*;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ResidenceDTO implements Serializable {
    @EqualsAndHashCode.Include
    private String id;
    private String name;
}
