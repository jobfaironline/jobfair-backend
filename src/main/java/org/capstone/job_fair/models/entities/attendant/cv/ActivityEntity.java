package org.capstone.job_fair.models.entities.attendant.cv;

import lombok.*;

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
    @Column(name = "id")
    private String id;
    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "function_title")
    private String functionTitle;
    @Basic
    @Column(name = "organization")
    private String organization;
    @Basic
    @Column(name = "from_date")
    private Long fromDate;
    @Basic
    @Column(name = "to_date")
    private Long toDate;
    @Basic
    @Column(name = "is_current_activity")
    private Boolean isCurrentActivity;
    @Basic
    @Column(name = "description")
    private String description;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActivityEntity that = (ActivityEntity) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(functionTitle, that.functionTitle))
            return false;
        if (!Objects.equals(organization, that.organization)) return false;
        if (!Objects.equals(fromDate, that.fromDate)) return false;
        if (!Objects.equals(toDate, that.toDate)) return false;
        if (!Objects.equals(isCurrentActivity, that.isCurrentActivity))
            return false;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (functionTitle != null ? functionTitle.hashCode() : 0);
        result = 31 * result + (organization != null ? organization.hashCode() : 0);
        result = 31 * result + (fromDate != null ? fromDate.hashCode() : 0);
        result = 31 * result + (toDate != null ? toDate.hashCode() : 0);
        result = 31 * result + (isCurrentActivity != null ? isCurrentActivity.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
