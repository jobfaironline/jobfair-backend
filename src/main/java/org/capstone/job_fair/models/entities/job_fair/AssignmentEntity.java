package org.capstone.job_fair.models.entities.job_fair;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.capstone.job_fair.models.entities.company.JobFairBoothEntity;
import org.capstone.job_fair.models.enums.AssignmentType;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assignment", schema = "dbo")
public class AssignmentEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private AssignmentType type;

    @Column(name = "create_time")
    private Long createTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_employee_id")
    private CompanyEmployeeEntity companyEmployee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_fair_booth_id")
    private JobFairBoothEntity jobFairBooth;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AssignmentEntity that = (AssignmentEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
