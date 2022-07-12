package org.capstone.job_fair.models.entities.attendant.cv;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

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

    @Column(name = "description_key_word")
    private String descriptionKeyWord;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CvWorkHistoryEntity entity = (CvWorkHistoryEntity) o;
        return id != null && Objects.equals(id, entity.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
