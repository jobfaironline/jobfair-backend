package org.capstone.job_fair.models.entities.attendant.cv;

import lombok.*;
import org.capstone.job_fair.models.entities.attendant.profile.CertificationEntity;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cv_certification", schema = "dbo")
public class CvCertificationEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "institution")
    private String institution;

    @Basic
    @Column(name = "issue_date")
    private Long issueDate;

    @Basic
    @Column(name = "expired_date")
    private Long expiredDate;

    @Basic
    @Column(name = "does_not_expired")
    private Boolean doesNotExpired;

    @Basic
    @Column(name = "certification_link")
    private String certificationLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cv_id")
    private CvEntity cv;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CertificationEntity that = (CertificationEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
