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
@Table(name = "application_reference", schema = "dbo")
public class ApplicationReferenceEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column(name = "full_name", length = 1000)
    private String fullName;
    @Column(name = "position", length = 100)
    private String position;
    @Column(name = "company", length = 1000)
    private String company;
    @Column(name = "email", length = 322)
    private String email;
    @Column(name = "phone_number", length = 11)
    private String phoneNumber;
    @ManyToOne()
    @JoinColumn(name = "application_id")
    private ApplicationEntity application;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ApplicationReferenceEntity that = (ApplicationReferenceEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
