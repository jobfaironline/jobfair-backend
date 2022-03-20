package org.capstone.job_fair.models.entities.attendant.profile;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "work_history", schema = "dbo")
public class WorkHistoryEntity {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        WorkHistoryEntity that = (WorkHistoryEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
