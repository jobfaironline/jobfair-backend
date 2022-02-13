package org.capstone.job_fair.models.dtos.job_fair;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.statuses.BoothStatus;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoothDTO implements Serializable {
    private String id;
    private Double price;
    private BoothStatus status;
    private String name;
    @JsonBackReference
    private LayoutDTO layout;
}
