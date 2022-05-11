package org.capstone.job_fair.models.entities.attendant.cv;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cv_work_history", schema = "dbo")
public class CvWorkHistoryEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Basic
    @Column(name = "position")
    private String position;
    @Basic
    @Column(name = "company")
    private String company;
    @Basic
    @Column(name = "from_date")
    private Long fromDate;
    @Basic
    @Column(name = "to_date")
    private Long toDate;
    @Basic
    @Column(name = "is_current_job")
    private Boolean isCurrentJob;
    @Basic
    @Column(name = "description")
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cv_id")
    private CvEntity cv;

}
