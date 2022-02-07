package org.capstone.job_fair.models.dtos.job_fair;

import lombok.Data;

import javax.persistence.Column;

@Data
public class DecoratedItemDTO {
    private String id;
    private Integer size;
    private String url;
    private String name;
}
