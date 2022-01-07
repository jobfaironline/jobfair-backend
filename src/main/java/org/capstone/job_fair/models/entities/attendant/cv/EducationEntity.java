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

    @Basic
    @Column(name = "qualification_id")
    private Integer qualificationId;
    @Basic
    @Column(name = "from_date")
    private Long fromDate;
    @Basic
    @Column(name = "to_date")
    private Long toDate;
    @Basic
    @Column(name = "achievement")
    private String achievement;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EducationEntity that = (EducationEntity) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(subject, that.subject)) return false;
        if (!Objects.equals(school, that.school)) return false;
        if (!Objects.equals(qualificationId, that.qualificationId))
            return false;
        if (!Objects.equals(toDate, that.toDate)) return false;
        if (!Objects.equals(fromDate, that.fromDate)) return false;
        return Objects.equals(achievement, that.achievement);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (school != null ? school.hashCode() : 0);
        result = 31 * result + (qualificationId != null ? qualificationId.hashCode() : 0);
        result = 31 * result + (fromDate != null ? fromDate.hashCode() : 0);
        result = 31 * result + (toDate != null ? toDate.hashCode() : 0);
        result = 31 * result + (achievement != null ? achievement.hashCode() : 0);
        return result;
    }
}
