package org.capstone.job_fair.models.dtos.job_fair;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class LayoutDTO implements Serializable {
    private String id;
    private String name;
    private String url;
}
