package org.capstone.job_fair.models.entities.company.layout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "decorator_booth_layout", schema = "dbo")
public class DecoratorBoothLayoutEntity {
    @Id
    private String id;
    @Column(name = "url")
    private String url;
    @Column(name = "name")
    private String name;
    @Column(name = "create_time")
    private Long createTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_employee_id", nullable = false)
    private CompanyEmployeeEntity companyEmployee;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "decorator_booth_layout_id", referencedColumnName = "id")
    private List<DecoratorBoothLayoutVideoEntity> decoratorBoothLayoutVideos;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DecoratorBoothLayoutEntity that = (DecoratorBoothLayoutEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
