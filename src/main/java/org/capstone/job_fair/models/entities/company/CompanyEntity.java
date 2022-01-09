package org.capstone.job_fair.models.entities.company;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "company", schema = "dbo")
public class CompanyEntity {
    @Id
    @Column(name = "id")
    private String id;
    @Basic
    @Column(name = "taxID", unique = true)
    private String taxId;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "address")
    private String address;
    @Basic
    @Column(name = "phone")
    private String phone;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "websiteURL")
    private String websiteUrl;
    @Basic
    @Column(name = "employee_max_num")
    private Integer employeeMaxNum;

    @ManyToOne
    @JoinColumn(name = "size_id")
    private CompanySizeEntity companySize;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanyEntity that = (CompanyEntity) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(taxId, that.taxId)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(address, that.address)) return false;
        if (!Objects.equals(phone, that.phone)) return false;
        if (!Objects.equals(email, that.email)) return false;
        if (!Objects.equals(employeeMaxNum, that.employeeMaxNum)) return false;
        if (!Objects.equals(websiteUrl, that.websiteUrl)) return false;
        if (!Objects.equals(companySize, that.companySize)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (taxId != null ? taxId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (websiteUrl != null ? websiteUrl.hashCode() : 0);
        result = 31 * result + (employeeMaxNum != null ? employeeMaxNum.hashCode() : 0);
        result = 31 * result + (companySize != null ? companySize.hashCode() : 0);
        return result;
    }
}
