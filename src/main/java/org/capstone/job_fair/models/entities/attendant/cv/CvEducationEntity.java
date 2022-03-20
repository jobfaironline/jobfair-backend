package org.capstone.job_fair.models.entities.attendant.cv;

import lombok.*;
import org.capstone.job_fair.models.entities.attendant.profile.CertificationEntity;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cv_education", schema = "dbo", catalog = "")
public class CvEducationEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Basic
    @Column(name = "subject")
    private String subject;
    @Basic
    @Column(name = "school")
    private String school;
    @Basic
    @Column(name = "from_date")
    private Long fromDate;
    @Basic
    @Column(name = "to_date")
    private Long toDate;
    @Basic
    @Column(name = "achievement")
    private String achievement;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cv_id")
    private CvEntity cv;
    @Basic
    @Column(name = "qualification_id")
    private Integer qualificationId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CertificationEntity that = (CertificationEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
