package org.capstone.job_fair.models.entities.company.job;

import lombok.*;
import org.capstone.job_fair.models.entities.attendant.JobLevelEntity;
import org.capstone.job_fair.models.entities.attendant.LanguageEntity;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.models.entities.company.SkillTagEntity;
import org.capstone.job_fair.models.entities.company.SubCategoryEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "job_position", schema = "dbo")
public class JobPositionEntity implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobPositionEntity that = (JobPositionEntity) o;

        return !Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (requirements != null ? requirements.hashCode() : 0);
        result = 31 * result + (minSalary != null ? minSalary.hashCode() : 0);
        result = 31 * result + (maxSalary != null ? maxSalary.hashCode() : 0);
        result = 31 * result + (contactPersonName != null ? contactPersonName.hashCode() : 0);
        result = 31 * result + (contactEmail != null ? contactEmail.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (jobLevel != null ? jobLevel.hashCode() : 0);
        result = 31 * result + (jobTypeEntity != null ? jobTypeEntity.hashCode() : 0);
        result = 31 * result + (company != null ? company.hashCode() : 0);
        return result;
    }
}
