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
@Table(name = "certification", schema = "dbo")
public class CertificationEntity {
    @Id
    @Column(name = "id")
    private String id;

    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "institution")
    private String institution;
    @Basic
    @Column(name = "year")
    private Integer year;
    @Basic
    @Column(name = "certification_link")
    private String certificationLink;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CertificationEntity that = (CertificationEntity) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(institution, that.institution)) return false;
        if (!Objects.equals(year, that.year)) return false;
        return Objects.equals(certificationLink, that.certificationLink);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (institution != null ? institution.hashCode() : 0);
        result = 31 * result + (year != null ? year.hashCode() : 0);
        result = 31 * result + (certificationLink != null ? certificationLink.hashCode() : 0);
        return result;
    }
}
