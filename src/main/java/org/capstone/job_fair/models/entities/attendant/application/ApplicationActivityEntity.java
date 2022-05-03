package org.capstone.job_fair.models.entities.attendant.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.capstone.job_fair.models.entities.attendant.cv.CvEntity;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "application_activity", schema = "dbo")
public class ApplicationActivityEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "function_title", length = 100)
    private String functionTitle;
    @Column(name = "organization", length = 100)
    private String organization;
    @Column(name = "from_date")
    private Long fromDate;
    @Column(name = "to_date")
    private Long toDate;
    @Column(name = "is_current_activity")
    private Boolean isCurrentActivity;
    @Column(name = "description", length = 5000)
    private String description;
    @ManyToOne()
    @JoinColumn(name = "application_id")
    private ApplicationEntity application;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ApplicationActivityEntity that = (ApplicationActivityEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
