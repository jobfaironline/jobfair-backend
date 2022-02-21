package org.capstone.job_fair.models.entities.job_fair;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.capstone.job_fair.models.statuses.ThreeDimensionMediaType;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "3d_media", schema = "dbo")
public class ThreeDimensionMedia {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;


    @Column(name = "url", nullable = false, length = 2048)
    private String url;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "description", nullable = false, length = 500)
    private String description;
    @Column(name = "type")
    @Enumerated(EnumType.ORDINAL)
    private ThreeDimensionMediaType type;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ThreeDimensionMedia that = (ThreeDimensionMedia) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
