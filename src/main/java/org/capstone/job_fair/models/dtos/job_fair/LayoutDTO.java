package org.capstone.job_fair.models.dtos.job_fair;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LayoutDTO implements Serializable {
    private String id;
    private String name;
    private String url;
    private String description;
    private Set<BoothDTO> booths;
}
