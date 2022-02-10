package org.capstone.job_fair.models.entities.job_fair;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "layout", schema = "dbo")
@NoArgsConstructor
@AllArgsConstructor
public class LayoutEntity {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    //do not use auto generated id here as we need pre generate id
    private String id;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "url", length = 2048)
    private String url;


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
