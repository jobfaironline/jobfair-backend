package org.capstone.job_fair.models.entities.company;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "media", schema = "dbo")
public class MediaEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "url", nullable = false, length = 2048)
    private String url;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "companyMedias")
    List<CompanyEntity> companies;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediaEntity that = (MediaEntity) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(url, that.url)) return false;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
