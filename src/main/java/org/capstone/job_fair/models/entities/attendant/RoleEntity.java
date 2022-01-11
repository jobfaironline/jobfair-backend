package org.capstone.job_fair.models.entities.attendant;

import lombok.*;
import org.capstone.job_fair.constants.RoleName;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "role", schema = "dbo")
public class RoleEntity {
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private RoleName name;
    @Basic
    @Column(name = "description")
    private String description;

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
