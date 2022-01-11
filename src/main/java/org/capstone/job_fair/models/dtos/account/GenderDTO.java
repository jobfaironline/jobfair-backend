package org.capstone.job_fair.models.dtos.account;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;

public class GenderDTO {
    private String id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "description")
    private String description;
}
