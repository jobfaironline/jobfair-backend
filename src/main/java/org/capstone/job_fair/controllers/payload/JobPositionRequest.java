package org.capstone.job_fair.controllers.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.capstone.job_fair.validators.EmailConstraint;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@ToString
public class JobPositionRequest {
    @NotNull
    private String id;
    @Size(max = 200)
    @Pattern(message="Type can contain alphanumeric characters only", regexp = "[a-zA-Z0-9 ]+")
    private String title;
    @Pattern(message="Type can contain alphanumeric characters only", regexp = "[a-zA-Z0-9 ]+")
    private String description;
    @Pattern(message="Type can contain alphanumeric characters only", regexp = "[a-zA-Z0-9 ]+")
    private String requirements;
    private Double minSalary;
    private Double maxSalary;
    @Size(max = 100)
    @Pattern(message="Type can contain alphanumeric characters only", regexp = "[a-zA-Z0-9 ]+")
    private String contactPerson;
    @Size(max = 322)
    @EmailConstraint
    private String contactEmail;
    private String preferredLanguageID;
    private String leveId;
    private String jobTypeId;
    private String locationId;
    private String companyId;
}
