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
@Table(name = "certification", schema = "dbo")
public class CertificationEntity {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "institution")
    private String institution;

    @Column(name = "year")
    private Integer year;

    @Column(name = "certification_link")
    private String certificationLink;

    @ManyToOne
    @JoinColumn(name = "attendant_id")
    private AttendantEntity attendant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CertificationEntity)) return false;
        CertificationEntity that = (CertificationEntity) o;
        return getId().equals(that.getId()) && getName().equals(that.getName()) && getInstitution().equals(that.getInstitution()) && getYear().equals(that.getYear()) && getCertificationLink().equals(that.getCertificationLink()) && getAttendant().equals(that.getAttendant());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getInstitution(), getYear(), getCertificationLink(), getAttendant());
    }
}
