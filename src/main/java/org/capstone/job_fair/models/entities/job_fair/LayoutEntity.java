package org.capstone.job_fair.models.entities.job_fair;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "layout", schema = "dbo")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LayoutEntity {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    //do not use auto generated id here as we need pre generate id
    private String id;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "url", length = 2048)
    private String url;
    @Column(name = "description", length = 500)
    private String description;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;

        LayoutEntity that = (LayoutEntity) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
