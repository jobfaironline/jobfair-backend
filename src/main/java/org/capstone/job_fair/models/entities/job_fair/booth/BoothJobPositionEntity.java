package org.capstone.job_fair.models.entities.job_fair.booth;

import lombok.*;
import org.capstone.job_fair.models.entities.attendant.misc.JobLevelEntity;
import org.capstone.job_fair.models.entities.attendant.misc.LanguageEntity;
import org.capstone.job_fair.models.entities.company.job.JobTypeEntity;
import org.capstone.job_fair.models.entities.company.misc.SkillTagEntity;
import org.capstone.job_fair.models.entities.company.misc.SubCategoryEntity;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "booth_job_position", schema = "dbo")
public class BoothJobPositionEntity {
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "booth_job_category",
            joinColumns = @JoinColumn(name = "registration_job_position_id"),
            inverseJoinColumns = @JoinColumn(name = "sub_category_id")
    )
    @ToString.Exclude
    Set<SubCategoryEntity> categories;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "booth_job_skill_tag",
            joinColumns = @JoinColumn(name = "registration_job_position_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_tag_id"))
    @ToString.Exclude
    Set<SkillTagEntity> skillTagEntities;

    @Column(name = "description")
    private String description;

    @Column(name = "requirements")
    private String requirements;

    @Column(name = "min_salary")
    private Double minSalary;

    @Column(name = "max_salary")
    private Double maxSalary;

    @Column(name = "num_of_position")
    private Integer numOfPosition;

    @Column(name = "location_id")
    private String locationId;

    @Column(name = "origin_job_position_id")
    private String originJobPosition;

    @Column(name = "note")
    private String note;

    @Column(name = "is_have_test")
    private Boolean isHaveTest;

    @Column(name = "test_time_length")
    private Integer testTimeLength;

    @Column(name = "num_of_question")
    private Integer numOfQuestion;

    @Column(name = "pass_mark")
    private Double passMark;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_fair_booth_id")
    private JobFairBoothEntity jobFairBooth;

    @Column(name = "description_key_word")
    private String descriptionKeyWord;

    @Column(name = "requirement_key_word")
    private String requirementKeyWord;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BoothJobPositionEntity that = (BoothJobPositionEntity) o;
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
                "contactEmail = " + contactEmail + ", " +
                "description = " + description + ", " +
                "requirements = " + requirements + ", " +
                "minSalary = " + minSalary + ", " +
                "maxSalary = " + maxSalary + ", " +
                "numOfPosition = " + numOfPosition + ", " +
                "locationId = " + locationId + ")";
    }
}
