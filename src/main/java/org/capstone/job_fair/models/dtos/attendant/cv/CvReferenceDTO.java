package org.capstone.job_fair.models.dtos.attendant.cv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CvReferenceDTO implements Serializable {
    private String id;
    private String fullName;
    private String position;
    private String company;
    private String email;
    private String phoneNumber;

}
