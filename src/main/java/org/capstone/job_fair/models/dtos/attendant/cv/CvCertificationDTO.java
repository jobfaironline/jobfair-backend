package org.capstone.job_fair.models.dtos.attendant.cv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CvCertificationDTO implements Serializable {

    private String id;
    private String name;
    private String institution;
    private Long expiredDate;
    private Long issueDate;
    private Boolean doesNotExpired;
    private String certificationLink;

}
