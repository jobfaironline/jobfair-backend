package org.capstone.job_fair.models.entities.attendant.cv;

import javax.persistence.*;
import java.util.Objects;

@Entity
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
    private Long fromMonth;
    @Basic
    @Column(name = "to_date", nullable = true)
    private Long toMonth;
    @Basic
    @Column(name = "is_current_job", nullable = true)
    private Integer isCurrentJob;
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
        if (!Objects.equals(fromMonth, that.fromMonth)) return false;
        if (!Objects.equals(toMonth, that.toMonth)) return false;
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
        result = 31 * result + (fromMonth != null ? fromMonth.hashCode() : 0);
        result = 31 * result + (toMonth != null ? toMonth.hashCode() : 0);
        result = 31 * result + (isCurrentJob != null ? isCurrentJob.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (cv != null ? cv.hashCode() : 0);
        return result;
    }
}
