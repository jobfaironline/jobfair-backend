package org.capstone.job_fair.models.entities.company;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "company_registration", schema = "dbo", catalog = "")
public class CompanyRegistrationEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "create_date")
    @CreatedDate
    private Long createDate;

    @Column(name = "description")
    private String description;
    @Column(name = "job_fair_id")
    private String jobFairId;
    @Column(name = "company_id")
    private String companyId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CompanyRegistrationEntity that = (CompanyRegistrationEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (createDate ^ (createDate >>> 32));
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (jobFairId != null ? jobFairId.hashCode() : 0);
        result = 31 * result + (companyId != null ? companyId.hashCode() : 0);
        return result;
    }
}
