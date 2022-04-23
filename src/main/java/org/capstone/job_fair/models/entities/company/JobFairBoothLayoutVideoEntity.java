package org.capstone.job_fair.models.entities.company;

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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "job_fair_booth_layout_video", schema = "dbo")
public class JobFairBoothLayoutVideoEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;
    @Column(name = "url", nullable = false, length = 2048)
    private String url;
    @Column(name = "item_name", nullable = false, length = 100)
    private String itemName;
    @Column(name = "job_fair_booth_layout_id", nullable = false, length = 36)
    private String jobFairBoothLayoutId;





    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        JobFairBoothLayoutVideoEntity that = (JobFairBoothLayoutVideoEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
