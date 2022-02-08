package org.capstone.job_fair.models.entities.company;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "registration_job_category", schema = "dbo", catalog = "")
public class RegistrationJobCategoryEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private String id;
    @Basic
    @Column(name = "registration_job_position_id")
    private String registrationJobPositionId;
    @Basic
    @Column(name = "sub_category_id")
    private Integer subCategoryId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MediaEntity that = (MediaEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
        public int hashCode() {return getClass().hashCode();}
}

