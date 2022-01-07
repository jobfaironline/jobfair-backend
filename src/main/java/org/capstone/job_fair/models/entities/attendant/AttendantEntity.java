package org.capstone.job_fair.models.entities.attendant;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "attendant", schema = "dbo")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AttendantEntity {
    @Id
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "firstname")
    private String firstname;
    @Basic
    @Column(name = "lastname")
    private String lastname;
    @Basic
    @Column(name = "middlename")
    private String middlename;
    @Basic
    @Column(name = "title")
    private String title;
    @Basic
    @Column(name = "phone")
    private String phone;
    @Basic
    @Column(name = "address")
    private String address;
    @Basic
    @Column(name = "dob")
    private Long dob;
    @Basic
    @Column(name = "status")
    private Integer status;
    @Basic
    @Column(name = "job_title")
    private String jobTitle;
    @Basic
    @Column(name = "year_of_exp")
    private Double yearOfExp;
    @Basic
    @Column(name = "marital_status")
    private Byte maritalStatus;
    @Basic
    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @JsonBackReference
    @ToString.Exclude
    private RoleEntity role;

    public String getFullname(){
        return firstname + " " + middlename + " " + lastname;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AttendantEntity that = (AttendantEntity) o;
        return email != null && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
