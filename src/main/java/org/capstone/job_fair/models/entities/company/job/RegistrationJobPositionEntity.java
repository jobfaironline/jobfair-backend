package org.capstone.job_fair.models.entities.company.job;

import lombok.*;
import org.capstone.job_fair.models.entities.attendant.JobLevelEntity;
import org.capstone.job_fair.models.entities.attendant.LanguageEntity;
import org.capstone.job_fair.models.entities.company.CompanyRegistrationEntity;
import org.capstone.job_fair.models.entities.company.SkillTagEntity;
import org.capstone.job_fair.models.entities.company.SubCategoryEntity;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "registration_job_position", schema = "dbo", catalog = "")
public class RegistrationJobPositionEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column(name = "title")
    private String title;
    @Column(name = "contact_person_name")
    private String contactPersonName;
    @Column(name = "contact_email")
    private String contactEmail;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preferred_language_id")
    private LanguageEntity language;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id")
    private JobLevelEntity jobLevel;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_type_id")
    private JobTypeEntity jobTypeEntity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_registration_id")
    private CompanyRegistrationEntity companyRegistration;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "registration_job_category",
            joinColumns = @JoinColumn(name = "registration_job_position_id"),
            inverseJoinColumns = @JoinColumn(name = "sub_category_id"))
    @ToString.Exclude
    Set<SubCategoryEntity> categories;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "registration_job_skill_tag",
            joinColumns = @JoinColumn(name = "registration_job_position_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_tag_id"))
    @ToString.Exclude
    Set<SkillTagEntity> skillTagEntities;

    @Column(name = "description")
    private String description;

    @Column(name = "requirements")
    private String requirements;

    @Column(name = "min_salary")
    private double minSalary;

    @Column(name = "max_salary")
    private double maxSalary;

    @Column(name = "num_of_position")
    private int numOfPosition;

    @Column(name = "location_id")
    private String locationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RegistrationJobPositionEntity that = (RegistrationJobPositionEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {return getClass().hashCode(); }
}
