package org.capstone.job_fair.models.entities.attendant.cv;

import lombok.*;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;

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
    @Column(name = "full_name", length = 100)
    private String fullName;
    @Column(name = "position", length = 100)
    private String position;
    @Column(name = "company", length = 1000)
    private String company;
    @Column(name = "email", length = 322)
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "attendant_id")
    private AttendantEntity attendant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReferenceEntity)) return false;
        ReferenceEntity that = (ReferenceEntity) o;
        return getId().equals(that.getId()) && getFullName().equals(that.getFullName()) && getPosition().equals(that.getPosition()) && getCompany().equals(that.getCompany()) && getEmail().equals(that.getEmail()) && getPhoneNumber().equals(that.getPhoneNumber()) && getAttendant().equals(that.getAttendant());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFullName(), getPosition(), getCompany(), getEmail(), getPhoneNumber(), getAttendant());
    }
}
