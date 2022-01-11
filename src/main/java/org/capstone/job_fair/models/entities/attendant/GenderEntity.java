package org.capstone.job_fair.models.entities.attendant;

import lombok.*;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "gender", schema = "dbo")
public class GenderEntity {
    @Id
    @Column(name = "id")
    private int id;
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

        GenderEntity that = (GenderEntity) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(name, that.name)) return false;
        return Objects.equals(description, that.description);
    }
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
