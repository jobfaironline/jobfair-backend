package org.capstone.job_fair.models.entities.job_fair;

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
@Table(name = "attendant_registration", schema = "dbo")
public class AttendantRegistrationEntity {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column(name = "attendant_id")
    private String attendantId;
    @Column(name = "job_fair_id")
    private String jobFairId;
    @Column(name = "create_time")
    private Long createTime;
    @Column(name = "is_visit")
    private Boolean isVisit;
    @Column(name = "visit_time")
    private Long visitTime;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AttendantRegistrationEntity that = (AttendantRegistrationEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
