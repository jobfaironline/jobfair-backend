package org.capstone.job_fair.models.entities.company.layout;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "decorator_booth_layout_video", schema = "dbo")
public class DecoratorBoothLayoutVideoEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column(name = "url", nullable = false, length = 2048)
    private String url;
    @Column(name = "item_name", nullable = false, length = 100)
    private String itemName;
    @Column(name = "decorator_booth_layout_id", nullable = false, length = 36)
    private String decoratorBoothLayoutId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DecoratorBoothLayoutVideoEntity that = (DecoratorBoothLayoutVideoEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
