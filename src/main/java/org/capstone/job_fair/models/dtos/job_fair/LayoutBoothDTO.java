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
public class LayoutBoothDTO implements Serializable {
    @EqualsAndHashCode.Include
    private String id;
    private BoothStatus status;
    private String name;
    @JsonBackReference
    private LayoutDTO layout;
    private Double x;
    private Double y;
    private Double z;
}
