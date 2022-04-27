package org.capstone.job_fair.models.dtos.attendant;

import lombok.*;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class JobLevelDTO implements Serializable {
    @EqualsAndHashCode.Include
    @NotNull
    @NumberFormat
    private String id;
    @NotBlank
    private String name;
}
