package org.capstone.job_fair.models.entities.company.job;

import lombok.*;
import org.capstone.job_fair.models.entities.attendant.JobLevelEntity;
import org.capstone.job_fair.models.entities.attendant.LanguageEntity;
import org.capstone.job_fair.models.entities.company.CompanyEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "job_position", schema = "dbo", catalog = "")
public class JobPositionEntity {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobPositionEntity that = (JobPositionEntity) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(title, that.title)) return false;
        if (!Objects.equals(description, that.description)) return false;
        if (!Objects.equals(requirements, that.requirements)) return false;
        if (!Objects.equals(minSalary, that.minSalary)) return false;
        if (!Objects.equals(maxSalary, that.maxSalary)) return false;
        if (!Objects.equals(contactPersonName, that.contactPersonName))
            return false;
        if (!Objects.equals(contactEmail, that.contactEmail)) return false;
        if (!Objects.equals(language, that.language)) return false;
        if (!Objects.equals(jobLevel, that.jobLevel)) return false;
        if (!Objects.equals(jobTypeEntity, that.jobTypeEntity)) return false;
        if (!Objects.equals(company, that.company)) return false;
        return true;
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
