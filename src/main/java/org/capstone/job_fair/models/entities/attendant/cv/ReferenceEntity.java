package org.capstone.job_fair.models.entities.attendant.cv;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reference", schema = "dbo")
public class ReferenceEntity {

    @Id
    @Column(name = "id")
    private String id;
    @Basic
    @Column(name = "full_name")
    private String fullName;
    @Basic
    @Column(name = "position")
    private String position;
    @Basic
    @Column(name = "company")
    private String company;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne
    @JoinColumn
    private CvEntity cv;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReferenceEntity that = (ReferenceEntity) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(fullName, that.fullName)) return false;
        if (!Objects.equals(position, that.position)) return false;
        if (!Objects.equals(company, that.company)) return false;
        if (!Objects.equals(email, that.email)) return false;
        if (!Objects.equals(cv, that.cv)) return false;
        return Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (company != null ? company.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (cv != null ? cv.hashCode() : 0);
        return result;
    }
}
