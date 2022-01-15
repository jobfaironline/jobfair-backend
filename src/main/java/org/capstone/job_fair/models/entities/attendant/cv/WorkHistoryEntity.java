package org.capstone.job_fair.models.entities.attendant.cv;

import org.capstone.job_fair.models.entities.attendant.AttendantEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "work_history", schema = "dbo")
public class WorkHistoryEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;
    @Column(name = "position", length = 100)
    private String position;
    @Column(name = "company", length = 100)
    private String company;
    @Column(name = "from_date")
    private Long fromDate;
    @Column(name = "to_date")
    private Long toDate;
    @Column(name = "is_current_job")
    private Boolean isCurrentJob;
    @Column(name = "description", nullable = true, length = 5000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "attendant_id")
    private AttendantEntity attendant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkHistoryEntity)) return false;
        WorkHistoryEntity that = (WorkHistoryEntity) o;
        return id.equals(that.id) && position.equals(that.position) && company.equals(that.company) && fromDate.equals(that.fromDate) && toDate.equals(that.toDate) && isCurrentJob.equals(that.isCurrentJob) && description.equals(that.description) && attendant.equals(that.attendant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, position, company, fromDate, toDate, isCurrentJob, description, attendant);
    }
}
