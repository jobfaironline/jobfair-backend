package org.capstone.job_fair.models.entities.company.job;

import lombok.*;

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
    @Basic
    @Column(name = "company_name")
    private String companyName;
    @Basic
    @Column(name = "company_size_id")
    private String companySizeId;
    @Basic
    @Column(name = "company_address")
    private String companyAddress;
    @Basic
    @Column(name = "company_profile")
    private String companyProfile;
    @Basic
    @Column(name = "company_logo_url")
    private String companyLogoUrl;

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
        if (!Objects.equals(companyName, that.companyName)) return false;
        if (!Objects.equals(companySizeId, that.companySizeId))
            return false;
        if (!Objects.equals(companyAddress, that.companyAddress))
            return false;
        if (!Objects.equals(companyProfile, that.companyProfile))
            return false;
        return Objects.equals(companyLogoUrl, that.companyLogoUrl);
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
        result = 31 * result + (companyName != null ? companyName.hashCode() : 0);
        result = 31 * result + (companySizeId != null ? companySizeId.hashCode() : 0);
        result = 31 * result + (companyAddress != null ? companyAddress.hashCode() : 0);
        result = 31 * result + (companyProfile != null ? companyProfile.hashCode() : 0);
        result = 31 * result + (companyLogoUrl != null ? companyLogoUrl.hashCode() : 0);
        return result;
    }
}
