package org.capstone.job_fair.models.dtos.job_fair;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.capstone.job_fair.models.statuses.BoothStatus;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BoothDTO implements Serializable {
    @EqualsAndHashCode.Include
    private String id;
    private Double price;
    private BoothStatus status;
    private String name;
    @JsonBackReference
    private LayoutDTO layout;
}
