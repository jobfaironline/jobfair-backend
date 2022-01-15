package org.capstone.job_fair.models.entities.company.job;

import lombok.*;
import org.capstone.job_fair.models.entities.attendant.JobLevelEntity;
import org.capstone.job_fair.models.entities.attendant.LanguageEntity;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.models.entities.company.SubCategoryEntity;

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
@Table(name = "job_position", schema = "dbo")
public class JobPositionEntity {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "requirements", nullable = false)
    private String requirements;

    @Column(name = "min_salary")
    private Double minSalary;

    @Column(name = "max_salary")
    private Double maxSalary;

    @Column(name = "contact_person_name", nullable = false)
    private String contactPersonName;

    @Column(name = "contact_email")
    private String contactEmail;

    @ManyToOne
    @JoinColumn(name = "preferred_language_id")
    private LanguageEntity language;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private JobLevelEntity jobLevel;

    @ManyToOne
    @JoinColumn(name = "job_type_id")
    private JobTypeEntity jobTypeEntity;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    @ManyToMany
    @JoinTable(
            name = "job_category",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "sub_category_id")
    )
    List<SubCategoryEntity> jobCategories;

    @ManyToMany
    @JoinTable(
            name = "job_position_skill_tag",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns =  @JoinColumn(name = "position_id")
    )
    List<SkillTag> jobPositionSkillTags;

}
