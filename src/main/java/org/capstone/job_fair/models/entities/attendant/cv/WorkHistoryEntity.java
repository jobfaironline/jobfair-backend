package org.capstone.job_fair.models.entities.attendant.cv;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "work_history", schema = "dbo")
public class WorkHistoryEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;
    @Basic
    @Column(name = "position", nullable = true, length = 100)
    private String position;
    @Basic
    @Column(name = "company", nullable = true, length = 100)
    private String company;
    @Basic
    @Column(name = "from_date", nullable = true)
    private Long fromDate;
    @Basic
    @Column(name = "to_date", nullable = true)
    private Long toDate;
    @Basic
    @Column(name = "is_current_job", nullable = true)
    private Boolean isCurrentJob;
    @Basic
    @Column(name = "description", nullable = true, length = 5000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "cv_id")
    private CvEntity cv;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkHistoryEntity that = (WorkHistoryEntity) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(position, that.position)) return false;
        if (!Objects.equals(company, that.company)) return false;
        if (!Objects.equals(fromDate, that.fromDate)) return false;
        if (!Objects.equals(toDate, that.toDate)) return false;
        if (!Objects.equals(isCurrentJob, that.isCurrentJob)) return false;
        if (!Objects.equals(description, that.description)) return false;
        if (!Objects.equals(cv, that.cv)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (company != null ? company.hashCode() : 0);
        result = 31 * result + (fromDate != null ? fromDate.hashCode() : 0);
        result = 31 * result + (toDate != null ? toDate.hashCode() : 0);
        result = 31 * result + (isCurrentJob != null ? isCurrentJob.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (cv != null ? cv.hashCode() : 0);
        return result;
    }
}
