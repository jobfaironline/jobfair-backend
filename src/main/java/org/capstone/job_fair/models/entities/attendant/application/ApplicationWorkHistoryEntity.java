package org.capstone.job_fair.models.entities.attendant.application;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "application_work_history", schema = "dbo")
public class ApplicationWorkHistoryEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
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
    @Column(name = "description", length = 5000)
    private String description;
    @ManyToOne()
    @JoinColumn(name = "application_id")
    private ApplicationEntity application;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ApplicationWorkHistoryEntity that = (ApplicationWorkHistoryEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
