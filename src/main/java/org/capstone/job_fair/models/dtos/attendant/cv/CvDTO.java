package org.capstone.job_fair.models.dtos.attendant.cv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.enums.JobLevel;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CvDTO implements Serializable {
    private String id;
    private AttendantDTO attendant;
    private String email;
    private String phone;
    private Integer yearOfExp;
    private JobLevel jobLevel;
    private String jobTitle;
    private List<CvSkillDTO> skills;
    private List<CvWorkHistoryDTO> workHistories;
    private List<CvEducationDTO> educations;
    private List<CvCertificationDTO> certifications;
    private List<CvReferenceDTO> references;
    private List<CvActivityDTO> activities;
}
