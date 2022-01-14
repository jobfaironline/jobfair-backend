package org.capstone.job_fair.models.entities.attendant.cv;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "education", schema = "dbo")
public class EducationEntity {
    @Id
    @Column(name = "id")
    private String id;
    @Basic
    @Column(name = "subject")
    private String subject;
    @Basic
    @Column(name = "school")
    private String school;
    @ManyToOne
    @JoinColumn(name = "qualification_id")
    private QualificationEntity qualification;
    @Basic
    @Column(name = "from_date")
    private Long fromDate;
    @Basic
    @Column(name = "to_date")
    private Long toDate;
    @Basic
    @Column(name = "achievement")
    private String achievement;

    @ManyToOne
    @JoinColumn(name = "cv_id")
    private CvEntity cv;

}
