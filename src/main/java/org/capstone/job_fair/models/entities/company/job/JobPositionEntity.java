package org.capstone.job_fair.models.entities.company.job;

import lombok.*;
import org.capstone.job_fair.models.entities.attendant.misc.JobLevelEntity;
import org.capstone.job_fair.models.entities.attendant.misc.LanguageEntity;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.models.entities.company.job.questions.QuestionsEntity;
import org.capstone.job_fair.models.entities.company.misc.SkillTagEntity;
import org.capstone.job_fair.models.entities.company.misc.SubCategoryEntity;
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
    @Column(name = "description")
    private String description;
    @Column(name = "requirements")
    private String requirements;
    @Column(name = "created_date")
    private Long createdDate;
    @Column(name = "last_updated_date")
    private Long updateDate;
    @Column(name = "description_key_word")
    private String descriptionKeyWord;
    @Column(name = "requirement_key_word")
    private String requirementKeyWord;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "jobPosition", orphanRemoval = true)
    private List<QuestionsEntity> questions;


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
