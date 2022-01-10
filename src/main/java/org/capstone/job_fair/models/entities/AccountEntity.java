package org.capstone.job_fair.models.entities;

import lombok.*;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.capstone.job_fair.models.entities.attendant.GenderEntity;
import org.capstone.job_fair.models.entities.attendant.RoleEntity;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.capstone.job_fair.models.entities.company.CompanyEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account", schema = "dbo")
public class AccountEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;
    @Basic
    @Column(name = "email", nullable = false, length = 322)
    private String email;
    @Basic
    @Column(name = "password", nullable = false, length = 50)
    private String password;
    @Basic
    @Column(name = "status", nullable = true)
    private Integer status;
    @Basic
    @Column(name = "phone", nullable = true, length = 11)
    private String phone;

    @Basic
    @Column(name = "profile_image_url", nullable = true, length = 2048)
    private String profileImageUrl;

    @Basic
    @Column(name = "firstname", nullable = true, length = 100)
    private String firstname;

    @Basic
    @Column(name = "lastname", nullable = true, length = 100)
    private String lastname;

    @Basic
    @Column(name = "middlename", nullable = true, length = 100)
    private String middlename;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @ManyToOne
    @JoinColumn(name = "gender_id")
    private GenderEntity gender;

    @OneToOne(mappedBy = "account")
    private AttendantEntity attendant;

    @OneToOne(mappedBy = "account")
    private CompanyEmployeeEntity employee;

    public String getFullname(){
        return firstname + " " + middlename + " " + lastname;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountEntity that = (AccountEntity) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(email, that.email)) return false;
        if (!Objects.equals(password, that.password)) return false;
        if (!Objects.equals(status, that.status)) return false;
        if (!Objects.equals(phone, that.phone)) return false;
        if (!Objects.equals(profileImageUrl, that.profileImageUrl))
            return false;
        if (!Objects.equals(firstname, that.firstname)) return false;
        if (!Objects.equals(lastname, that.lastname)) return false;
        if (!Objects.equals(role, that.role)) return false;
        if (!Objects.equals(gender, that.gender)) return false;
        return Objects.equals(middlename, that.middlename);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (profileImageUrl != null ? profileImageUrl.hashCode() : 0);
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (middlename != null ? middlename.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        return result;
    }
}
