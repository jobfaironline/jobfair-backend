package org.capstone.job_fair.models.entities.attendant.cv;

import lombok.*;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.capstone.job_fair.models.entities.attendant.QualificationEntity;

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
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "subject", nullable = false, length = 100)
    private String subject;

    @Column(name = "school", length = 100)
    private String school;

    @Column(name = "from_date")
    private Long fromDate;

    @Column(name = "to_date")
    private Long toDate;

    @Column(name = "achievement")
    private String achievement;

    @ManyToOne
    @JoinColumn(name = "attendant_id")
    private AttendantEntity attendant;

    @ManyToOne
    @JoinColumn(name = "qualification_id")
    private QualificationEntity qualification;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EducationEntity)) return false;
        EducationEntity that = (EducationEntity) o;
        return getId().equals(that.getId()) && getSubject().equals(that.getSubject()) && getSchool().equals(that.getSchool()) && getFromDate().equals(that.getFromDate()) && getToDate().equals(that.getToDate()) && getAchievement().equals(that.getAchievement()) && getAttendant().equals(that.getAttendant()) && getQualification().equals(that.getQualification());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSubject(), getSchool(), getFromDate(), getToDate(), getAchievement(), getAttendant(), getQualification());
    }
}
