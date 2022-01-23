package org.capstone.job_fair.models.entities.attendant.cv;

import lombok.*;
import org.capstone.job_fair.models.enums.Qualification;
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
@Table(name = "education", schema = "dbo")
public class EducationEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
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

    @Column(name = "qualification_id", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Qualification qualification;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EducationEntity that = (EducationEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
