package org.capstone.job_fair.models.entities.company.job;

import lombok.*;
import org.capstone.job_fair.models.entities.attendant.JobLevelEntity;
import org.capstone.job_fair.models.entities.attendant.LanguageEntity;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "job_position", schema = "dbo")
public class JobPositionEntity {
    @Id
    @Column(name = "id")
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
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "job_category",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "sub_category_id"))
    @ToString.Exclude
    Set<SubCategoryEntity> categories;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "job_position_skill_tag",
            joinColumns = @JoinColumn(name = "position_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @ToString.Exclude
    Set<SkillTagEntity> skillTagEntities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        JobPositionEntity that = (JobPositionEntity) o;
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
                "title = " + title + ", " +
                "contactPersonName = " + contactPersonName + ", " +
                "contactEmail = " + contactEmail + ")";
    }
}
