package org.capstone.job_fair.models.entities.job_fair;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "layout", schema = "dbo")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LayoutEntity {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "url", length = 2048)
    private String url;
    @Column(name = "description", length = 500)
    private String description;
    @OneToMany(mappedBy = "layout", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<BoothEntity> booths;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;

        LayoutEntity that = (LayoutEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
