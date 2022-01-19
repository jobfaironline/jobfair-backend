package org.capstone.job_fair.models.entities.company.job;

import lombok.*;
import org.capstone.job_fair.models.entities.attendant.JobLevelEntity;
import org.capstone.job_fair.models.entities.attendant.LanguageEntity;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.models.entities.company.SkillTagEntity;
import org.capstone.job_fair.models.entities.company.SubCategoryEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "job_position", schema = "dbo")
public class JobPositionEntity {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id")
    private String id;
    @Basic
    @Column(name = "title")
    private String title;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "requirements")
    private String requirements;
    @Basic
    @Column(name = "min_salary")
    private Double minSalary;
    @Basic
    @Column(name = "max_salary")
    private Double maxSalary;
    @Basic
    @Column(name = "contact_person_name")
    private String contactPersonName;
    @Basic
    @Column(name = "contact_email")
    private String contactEmail;
    @Column(name = "num_of_emp")
    private Integer numOfEmp;
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
            inverseJoinColumns = @JoinColumn(name = "sub_category_id"))
    List<SubCategoryEntity> categories;

    @ManyToMany
    @JoinTable(
            name = "job_position_skill_tag",
            joinColumns = @JoinColumn(name = "position_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    List<SkillTagEntity> skillTagEntities;


}
