package org.capstone.job_fair.models.entities.attendant.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "application_certification", schema = "dbo")
public class ApplicationCertificationEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "institution", length = 100)
    private String institution;
    @Column(name = "issue_date")
    private Long issueDate;
    @Column(name = "certification_link", length = 2048)
    private String certificationLink;
    @Column(name = "expired_date")
    private Long expiredDate;
    @Column(name = "does_not_expired")
    private Boolean doesNotExpired;
    @ManyToOne()
    @JoinColumn(name = "application_id")
    private ApplicationEntity application;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ApplicationCertificationEntity that = (ApplicationCertificationEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
