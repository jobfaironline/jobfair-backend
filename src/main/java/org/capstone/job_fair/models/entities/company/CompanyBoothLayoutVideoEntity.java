package org.capstone.job_fair.models.entities.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "company_booth_layout_video", schema = "dbo")
public class CompanyBoothLayoutVideoEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;
    @Column(name = "url", nullable = false, length = 2048)
    private String url;
    @Column(name = "item_name", nullable = false, length = 100)
    private String itemName;
    @Column(name = "company_booth_layout_id", nullable = false, length = 36)
    private String companyBoothLayoutId;





    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CompanyBoothLayoutVideoEntity that = (CompanyBoothLayoutVideoEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
