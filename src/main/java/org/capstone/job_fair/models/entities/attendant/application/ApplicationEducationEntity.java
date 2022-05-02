package org.capstone.job_fair.models.entities.attendant.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.capstone.job_fair.models.enums.Qualification;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "application_education", schema = "dbo")
public class ApplicationEducationEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column(name = "subject", length = 100)
    private String subject;
    @Column(name = "school", length = 100)
    private String school;
    @Column(name = "from_date")
    private Long fromDate;
    @Column(name = "to_date")
    private Long toDate;
    @Column(name = "achievement", length = 5000)
    private String achievement;
    @ManyToOne()
    @JoinColumn(name = "application_id")
    private ApplicationEntity application;
    @Column(name = "qualification_id")
    @Enumerated(EnumType.ORDINAL)
    private Qualification qualificationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ApplicationEducationEntity that = (ApplicationEducationEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
