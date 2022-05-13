package org.capstone.job_fair.models.entities.job_fair;

import lombok.*;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "layout", schema = "dbo")
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
    private Set<LayoutBoothEntity> booths;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyEntity company;
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;


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
