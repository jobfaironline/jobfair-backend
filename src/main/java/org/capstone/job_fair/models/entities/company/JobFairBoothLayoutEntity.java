package org.capstone.job_fair.models.entities.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "job_fair_booth_layout", schema = "dbo")
public class JobFairBoothLayoutEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;
    @Column(name = "version", nullable = false)
    private Integer version;
    @Column(name = "create_date", nullable = false)
    private Long createDate;
    @ManyToOne
    @JoinColumn(name = "job_fair_booth_id")
    private JobFairBoothEntity jobFairBooth;
    @Column(name = "url", nullable = false)
    private String url;

    @OneToMany
    @JoinColumn(name = "job_fair_booth_layout_id", referencedColumnName = "id")
    List<JobFairBoothLayoutVideoEntity> companyBoothLayoutVideos;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        JobFairBoothEntity that = (JobFairBoothEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
