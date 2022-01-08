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
@Table(name = "cv", schema = "dbo")
public class CvEntity {
    @Id
    @Column(name = "id")
    private String id;
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
    @Column(name = "year_of_exp")
    private Double yearOfExp;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "cell_number")
    private String cellNumber;
    @Basic
    @Column(name = "dob")
    private Long dob;
    @Basic
    @Column(name = "marital_status")
    private Boolean maritalStatus;
    @Basic
    @Column(name = "address")
    private String address;
    @Basic
    @Column(name = "summary")
    private String summary;
    @Basic
    @Column(name = "create_date")
    private Long createDate;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CvEntity cvEntity = (CvEntity) o;

        if (!Objects.equals(id, cvEntity.id)) return false;
        if (!Objects.equals(firstname, cvEntity.firstname)) return false;
        if (!Objects.equals(lastname, cvEntity.lastname)) return false;
        if (!Objects.equals(middlename, cvEntity.middlename)) return false;
        if (!Objects.equals(yearOfExp, cvEntity.yearOfExp)) return false;
        if (!Objects.equals(email, cvEntity.email)) return false;
        if (!Objects.equals(cellNumber, cvEntity.cellNumber)) return false;
        if (!Objects.equals(dob, cvEntity.dob)) return false;
        if (!Objects.equals(maritalStatus, cvEntity.maritalStatus))
            return false;
        if (!Objects.equals(address, cvEntity.address)) return false;
        if (!Objects.equals(summary, cvEntity.summary)) return false;
        return Objects.equals(createDate, cvEntity.createDate);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (middlename != null ? middlename.hashCode() : 0);
        result = 31 * result + (yearOfExp != null ? yearOfExp.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (cellNumber != null ? cellNumber.hashCode() : 0);
        result = 31 * result + (dob != null ? dob.hashCode() : 0);
        result = 31 * result + (maritalStatus != null ? maritalStatus.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        return result;
    }
}
