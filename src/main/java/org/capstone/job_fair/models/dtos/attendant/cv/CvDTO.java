package org.capstone.job_fair.models.dtos.attendant.cv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CvDTO implements Serializable {
    private String id;
    private AttendantDTO attendant;
    private String email;
    private String phone;
    private Integer yearOfExp;
    private Integer jobLevel;
    private String title;
}
