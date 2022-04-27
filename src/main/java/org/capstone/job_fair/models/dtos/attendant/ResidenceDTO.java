package org.capstone.job_fair.models.dtos.attendant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ResidenceDTO implements Serializable {
    @EqualsAndHashCode.Include
    private Integer id;
    @NotBlank
    private String name;
}
