package org.capstone.job_fair.models.entities.account;

import lombok.*;

import javax.persistence.*;

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
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenderEntity gender = (GenderEntity) o;
        return id.equals(gender.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
