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
@Table(name = "activity", schema = "dbo")
public class ActivityEntity {
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

    @ManyToOne
    @JoinColumn(name = "attendant_id")
    private AttendantEntity attendant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActivityEntity)) return false;
        ActivityEntity that = (ActivityEntity) o;
        return getId().equals(that.getId()) && getName().equals(that.getName()) && getFunctionTitle().equals(that.getFunctionTitle()) && getOrganization().equals(that.getOrganization()) && getFromDate().equals(that.getFromDate()) && getToDate().equals(that.getToDate()) && getIsCurrentActivity().equals(that.getIsCurrentActivity()) && getDescription().equals(that.getDescription()) && getAttendant().equals(that.getAttendant());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getFunctionTitle(), getOrganization(), getFromDate(), getToDate(), getIsCurrentActivity(), getDescription(), getAttendant());
    }
}
