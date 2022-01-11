package org.capstone.job_fair.models.entities.attendant;

import lombok.*;

import javax.persistence.*;

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
    private Integer id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "description")
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoleEntity role = (RoleEntity) o;

        return id.equals(role.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
