package org.capstone.job_fair.models.entities.company;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.capstone.job_fair.models.entities.company.misc.CompanyBenefitEntity;
import org.capstone.job_fair.models.entities.company.misc.CompanySizeEntity;
import org.capstone.job_fair.models.entities.company.misc.SubCategoryEntity;
import org.capstone.job_fair.models.statuses.CompanyStatus;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "company", schema = "dbo")
public class CompanyEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column(name = "taxID", unique = true, length = 9, nullable = false)
    private String taxId;
    @Column(name = "name", length = 1000)
    private String name;
    @Column(name = "address", length = 1000)
    private String address;
    @Column(name = "phone", length = 11)
    private String phone;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "websiteURL")
    private String websiteUrl;
    @Column(name = "employee_max_num")
    private Integer employeeMaxNum;
    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private CompanyStatus status;
    @Column(name = "company_logo_url")
    private String companyLogoURL;
    @Column(name = "description")
    private String companyDescription;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "size_id")
    private CompanySizeEntity companySize;
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<CompanyMediaEntity> medias;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "company_category",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "sub_category_id")
    )
    private Set<SubCategoryEntity> subCategories;

    @OneToMany(mappedBy = "company")
    private Set<CompanyBenefitEntity> companyBenefits;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CompanyEntity that = (CompanyEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "taxId = " + taxId + ", " +
                "name = " + name + ", " +
                "address = " + address + ", " +
                "phone = " + phone + ", " +
                "email = " + email + ", " +
                "websiteUrl = " + websiteUrl + ", " +
                "employeeMaxNum = " + employeeMaxNum + ", " +
                "status = " + status + ")";
    }
}
