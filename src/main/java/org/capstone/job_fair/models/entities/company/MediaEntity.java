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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "media", schema = "dbo")
public class MediaEntity {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "url", nullable = false, length = 2048)
    private String url;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

}
