package org.capstone.job_fair.models.entities.company;

import lombok.*;
import org.capstone.job_fair.models.entities.attendant.cv.SkillEntity;
import org.capstone.job_fair.models.statuses.CompanyStatus;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "company", schema = "dbo")
public class CompanyEntity {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "taxID", unique = true, length = 9, nullable = false)
    private String taxId;

    @Column(name = "name", length = 1000)
    private String name;

    @Column(name = "address",  length = 1000)
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

    @ManyToOne
    @JoinColumn(name = "size_id")
    private CompanySizeEntity companySize;

    @OneToMany(mappedBy = "company")
    private List<MediaEntity> medias;

    @ManyToMany
    @JoinTable(
            name = "company_benefit",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns =  @JoinColumn(name = "benefit_id")
    )
    List<BenefitEntity> companyBenefits;

    @ManyToMany
    @JoinTable(
            name = "company_category",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "sub_category_id")
    )
    List<SubCategoryEntity> companySubCategory;

}
