package org.capstone.job_fair.models.entities.attendant.cv;

import lombok.*;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "activity", schema = "dbo")
public class ActivityEntity {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "function_title", nullable = false, length = 100)
    private String functionTitle;

    @Column(name = "organization", nullable = false, length = 100)
    private String organization;

    @Column(name = "from_date")
    private Long fromDate;

    @Column(name = "to_date")
    private Long toDate;

    @Column(name = "is_current_activity")
    private Boolean isCurrentActivity;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendant_id")
    private AttendantEntity attendant;

}
